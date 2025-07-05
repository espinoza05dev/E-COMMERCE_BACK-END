package saeg.ecommerceback.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import saeg.ecommerceback.security.UserPrincipal;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String jwtSecret;
    private final int jwtExpirationInMs;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
                            @Value("${jwt.expiration}") int jwtExpirationInMs,
                            RedisTemplate<String, String> redisTemplate) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(UserPrincipal userPrincipal) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("email", userPrincipal.getEmail())
                .claim("name", userPrincipal.getFullName())
                .claim("role", userPrincipal.getUser().getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Date expiryDate = new Date(System.currentTimeMillis() + (jwtExpirationInMs * 7)); // 7 veces más tiempo

        String refreshToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Guardar refresh token en Redis
        String redisKey = "refresh_token:" + userPrincipal.getId();
        redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofDays(7));

        return refreshToken;
    }

    public Long getUserIdFromJWT(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);

            // Verificar si el token está en blacklist
            String tokenId = getTokenId(authToken);
            String blacklistKey = "blacklist:" + tokenId;
            return !redisTemplate.hasKey(blacklistKey);

        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }

    private String getTokenId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject() + "_" + claims.getIssuedAt().getTime();
    }

    public void blacklistToken(String token) {
        String tokenId = getTokenId(token);
        String blacklistKey = "blacklist:" + tokenId;

        // Calcular tiempo restante del token
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        long timeToExpiry = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (timeToExpiry > 0) {
            redisTemplate.opsForValue().set(blacklistKey, "true", Duration.ofMillis((timeToExpiry)));
        }
    }

    public boolean validateRefreshToken(String refreshToken, Long userId) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Long tokenUserId = Long.parseLong(claims.getSubject());

            if (!tokenUserId.equals(userId)) {
                return false;
            }

            // Verificar que el refresh token existe en Redis
            String redisKey = "refresh_token:" + userId;
            String storedToken = redisTemplate.opsForValue().get(redisKey);

            return refreshToken.equals(storedToken);

        } catch (Exception ex) {
            logger.error("Error validating refresh token", ex);
            return false;
        }
    }

    public void revokeRefreshToken(Long userId) {
        String redisKey = "refresh_token:" + userId;
        redisTemplate.delete(redisKey);
    }
}
