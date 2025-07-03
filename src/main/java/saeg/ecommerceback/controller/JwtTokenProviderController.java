package saeg.ecommerceback.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProviderController {
    private final String jwtSecret;
    private final long jwtExpiration;

    public JwtTokenProviderController(@Value("${jwt.secret}") String jwtSecret,
                                      @Value("${jwt.expiration}") long jwtExpiration){
        if (jwtSecret == null || jwtSecret.length() < 32){
            throw new IllegalArgumentException("jwtSecret length must be >= 32");
        }
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }
}
