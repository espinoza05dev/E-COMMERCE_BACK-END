package saeg.ecommerceback.security.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(SecurityConfigRedis.class)
class SecurityConfigRedisTest {

    private SecurityConfigRedis securityConfig;
    private MockMvc mockMvc;

    @Test
    void testPasswordEncoder_ReturnsNotNull() {
        // Arrange
        securityConfig = new SecurityConfigRedis();

        // Act
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertEquals("org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder",
                encoder.getClass().getName());
    }

    @Test
    void testPasswordEncoder_EncodesPassword() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";

        // Act
        String encodedPassword = encoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testUserDetailsService_ReturnsNotNull() {
        // Arrange
        securityConfig = new SecurityConfigRedis();

        // Act
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Assert
        assertNotNull(userDetailsService);
    }

    @Test
    void testUserDetailsService_AdminUserExists() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUsername());
        assertTrue(adminUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(adminUser.isEnabled());
        assertTrue(adminUser.isAccountNonExpired());
        assertTrue(adminUser.isAccountNonLocked());
        assertTrue(adminUser.isCredentialsNonExpired());
    }

    @Test
    void testUserDetailsService_RegularUserExists() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act
        UserDetails testUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertNotNull(testUser);
        assertEquals("user", testUser.getUsername());
        assertTrue(testUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
        assertTrue(testUser.isEnabled());
        assertTrue(testUser.isAccountNonExpired());
        assertTrue(testUser.isAccountNonLocked());
        assertTrue(testUser.isCredentialsNonExpired());
    }

    @Test
    void testUserDetailsService_AdminPasswordIsEncoded() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(adminUser.getPassword());
        assertNotEquals("admin123", adminUser.getPassword());
        assertTrue(encoder.matches("admin123", adminUser.getPassword()));
    }

    @Test
    void testUserDetailsService_UserPasswordIsEncoded() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Act
        UserDetails testUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertNotNull(testUser.getPassword());
        assertNotEquals("user123", testUser.getPassword());
        assertTrue(encoder.matches("user123", testUser.getPassword()));
    }

    @Test
    void testUserDetailsService_NonExistentUser() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act & Assert
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    void testUsersHaveDifferentPasswords() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");
        UserDetails testUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertNotEquals(adminUser.getPassword(), testUser.getPassword());
    }

    @Test
    void testUsersHaveDifferentRoles() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");
        UserDetails testUser = userDetailsService.loadUserByUsername("user");

        // Assert
        boolean adminHasAdminRole = adminUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean userHasUserRole = testUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        boolean adminHasUserRole = adminUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        assertTrue(adminHasAdminRole);
        assertTrue(userHasUserRole);
        assertFalse(adminHasUserRole);
    }

    @Test
    void testPasswordEncoder_SamePasswordProducesDifferentHashes() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "testPassword";

        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        // Assert
        assertNotEquals(hash1, hash2);
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }

    @Test
    void testPasswordEncoder_WrongPasswordDoesNotMatch() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        // Act
        String encodedPassword = encoder.encode(correctPassword);

        // Assert
        assertTrue(encoder.matches(correctPassword, encodedPassword));
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    void testUserDetailsService_UsersAreEnabled() {
        // Arrange
        securityConfig = new SecurityConfigRedis();
        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // Act
        UserDetails adminUser = userDetailsService.loadUserByUsername("admin");
        UserDetails testUser = userDetailsService.loadUserByUsername("user");

        // Assert
        assertTrue(adminUser.isEnabled());
        assertTrue(testUser.isEnabled());
        assertTrue(adminUser.isAccountNonExpired());
        assertTrue(testUser.isAccountNonExpired());
        assertTrue(adminUser.isAccountNonLocked());
        assertTrue(testUser.isAccountNonLocked());
        assertTrue(adminUser.isCredentialsNonExpired());
        assertTrue(testUser.isCredentialsNonExpired());
    }
}