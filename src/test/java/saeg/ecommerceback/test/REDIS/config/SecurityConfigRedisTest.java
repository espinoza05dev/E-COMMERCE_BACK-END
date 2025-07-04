package saeg.ecommerceback.test.REDIS.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import saeg.ecommerceback.security.config.SecurityConfigRedis;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig
class SecurityConfigRedisTest {
    @Mock
    private HttpSecurity httpSecurity;

    @InjectMocks
    private SecurityConfigRedis securityConfigRedis;

    @Test
    void testPasswordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Act
        PasswordEncoder encoder = securityConfigRedis.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testPasswordEncoder_ShouldEncodePasswordCorrectly() {
        // Arrange
        PasswordEncoder encoder = securityConfigRedis.passwordEncoder();
        String rawPassword = "testPassword";

        // Act
        String encodedPassword = encoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testUserDetailsService_ShouldReturnInMemoryUserDetailsManager() {
        // Act
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();

        // Assert
        assertNotNull(userDetailsService);
        assertTrue(userDetailsService instanceof InMemoryUserDetailsManager);
    }

    @Test
    void testUserDetailsService_ShouldContainAdminUser() {
        // Act
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUsername());
        assertTrue(adminUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserDetailsService_ShouldContainRegularUser() {
        // Act
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();
        UserDetails user = userDetailsService.loadUserByUsername("user");

        // Assert
        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertTrue(user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testUserDetailsService_ShouldEncodePasswords() {
        // Arrange
        PasswordEncoder encoder = securityConfigRedis.passwordEncoder();
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");
        UserDetails regularUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertNotNull(adminUser.getPassword());
        assertNotNull(regularUser.getPassword());

        // Verify passwords are encoded (not plain text)
        assertNotEquals("admin123", adminUser.getPassword());
        assertNotEquals("user123", regularUser.getPassword());

        // Verify passwords can be matched
        assertTrue(encoder.matches("admin123", adminUser.getPassword()));
        assertTrue(encoder.matches("user123", regularUser.getPassword()));
    }

    @Test
    void testUserDetailsService_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();

        // Act & Assert
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testPasswordEncoder_ShouldNotMatchIncorrectPassword() {
        // Arrange
        PasswordEncoder encoder = securityConfigRedis.passwordEncoder();
        String rawPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = encoder.encode(rawPassword);

        // Act & Assert
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    void testUserDetailsService_AdminUserShouldBeEnabled() {
        // Act
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertTrue(adminUser.isEnabled());
        assertTrue(adminUser.isAccountNonExpired());
        assertTrue(adminUser.isAccountNonLocked());
        assertTrue(adminUser.isCredentialsNonExpired());
    }

    @Test
    void testUserDetailsService_RegularUserShouldBeEnabled() {
        // Act
        UserDetailsService userDetailsService = securityConfigRedis.userDetailsService();
        UserDetails regularUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertTrue(regularUser.isEnabled());
        assertTrue(regularUser.isAccountNonExpired());
        assertTrue(regularUser.isAccountNonLocked());
        assertTrue(regularUser.isCredentialsNonExpired());
    }

    @Test
    void testPasswordEncoder_ShouldGenerateDifferentHashesForSamePassword() {
        // Arrange
        PasswordEncoder encoder = securityConfigRedis.passwordEncoder();
        String password = "samePassword";

        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        // Assert
        assertNotEquals(hash1, hash2, "BCrypt should generate different hashes for same password");
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }
}