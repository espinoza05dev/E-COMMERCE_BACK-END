package saeg.ecommerceback.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private AuthController.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new AuthController.LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpass");
    }

    @Test
    void testLoginSuccess() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("session123");

        // When
        ResponseEntity<Map<String, String>> response = authController.login(loginRequest, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login exitoso", response.getBody().get("message"));
        assertEquals("testuser", response.getBody().get("username"));
        assertEquals("session123", response.getBody().get("sessionId"));

        verify(httpSession).setAttribute("user", "testuser");
        verify(httpSession).setAttribute("authorities", authentication.getAuthorities());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginFailure() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When
        ResponseEntity<Map<String, String>> response = authController.login(loginRequest, httpServletRequest);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Credenciales inválidas", response.getBody().get("error"));
        verify(httpServletRequest, never()).getSession(true);
    }

    @Test
    void testGetCurrentUserSuccess() {
        // Given
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection)List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("session123");

        // When
        ResponseEntity<Map<String, Object>> response = authController.getCurrentUser(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().get("username"));
        assertEquals("session123", response.getBody().get("sessionId"));
        assertEquals(authentication.getAuthorities(), response.getBody().get("authorities"));
    }

    @Test
    void testGetCurrentUserNoAuthentication() {
        // When
        ResponseEntity<Map<String, Object>> response = authController.getCurrentUser(null, httpServletRequest);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No autenticado", response.getBody().get("error"));
    }

    @Test
    void testGetCurrentUserNoSession() {
        // Given
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection)List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(false)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = authController.getCurrentUser(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().get("username"));
        assertNull(response.getBody().get("sessionId"));
    }

    @Test
    void testLogoutWithSession() {
        // Given
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);

        // When
        ResponseEntity<String> response = authController.logout(httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout exitoso", response.getBody());
        verify(httpSession).invalidate();
    }

    @Test
    void testLogoutWithoutSession() {
        // Given
        when(httpServletRequest.getSession(false)).thenReturn(null);

        // When
        ResponseEntity<String> response = authController.logout(httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout exitoso", response.getBody());
        verify(httpSession, never()).invalidate();
    }

    @Test
    void testLoginRequestGettersAndSetters() {
        // Given
        AuthController.LoginRequest request = new AuthController.LoginRequest();

        // When
        request.setUsername("testuser");
        request.setPassword("testpass");

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("testpass", request.getPassword());
    }
}