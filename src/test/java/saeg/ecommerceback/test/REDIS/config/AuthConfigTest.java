package saeg.ecommerceback.test.REDIS.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import saeg.ecommerceback.auth.AuthConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class AuthConfigTest {
    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthConfig authConfig;

    @Test
    void testAuthenticationManager_ShouldReturnAuthenticationManager() throws Exception {
            when((Iterable<? extends Publisher<?>>) authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

            AuthenticationManager res = authConfig.authenticationManager(authenticationConfiguration);

            assertNotNull(res);
            assertEquals(authenticationManager, res);
            verify(authenticationConfiguration).getAuthenticationManager();
    }

    @Test
    void testAuthenticationManager_ShouldThrowException_WhenConfigurationThrowsException() throws Exception {
        //Arrange
        Exception ExpectedException = new RuntimeException("Authentification configuration error");
        when((Iterable<? extends Publisher<?>>) authenticationConfiguration.getAuthenticationManager()).thenReturn(ExpectedException);

        //Act and Assert
        Exception thrownException = assertThrows(RuntimeException.class, () -> authConfig.authenticationManager(authenticationConfiguration));

        assertEquals(ExpectedException, thrownException);
        verify(authenticationConfiguration).getAuthenticationManager();
    }

    @Test
    void testAuthenticationManager_ShouldNotReturnNull() throws Exception {
        // Arrange
        when((Publisher<?>) authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        // Act
        AuthenticationManager result = authConfig.authenticationManager(authenticationConfiguration);

        // Assert
        assertNotNull(result, "AuthenticationManager should not be null");
    }
}