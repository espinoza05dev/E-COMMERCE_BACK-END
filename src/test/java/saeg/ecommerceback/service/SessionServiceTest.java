package saeg.ecommerceback.service;

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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SessionService sessionService;

    private final String testUsername = "testUser";
    private final String testSessionId = "test-session-123";
    private final String testLoginTime = "2024-01-15T10:30:00";
    private final long testCreationTime = 1705312200000L; // 2024-01-15T10:30:00
    private final long testLastAccessTime = 1705312800000L; // 2024-01-15T10:40:00
    private final int testMaxInactiveInterval = 1800; // 30 minutos

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(request, session);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);

        // Act
        ResponseEntity<String> response = sessionService.login(testUsername, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(testUsername));
        assertTrue(response.getBody().contains(testSessionId));

        // Verify session attributes were set
        verify(session).setAttribute("username", testUsername);
        verify(session).setAttribute(eq("loginTime"), any(String.class));
        verify(request).getSession(true);
    }

    @Test
    void testLogin_WithDifferentUsername() {
        // Arrange
        String differentUsername = "anotherUser";
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);

        // Act
        ResponseEntity<String> response = sessionService.login(differentUsername, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(differentUsername));
        verify(session).setAttribute("username", differentUsername);
    }

    @Test
    void testGetCurrentUser_WithActiveSession() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(session.getAttribute("loginTime")).thenReturn(testLoginTime);
        when(session.getMaxInactiveInterval()).thenReturn(testMaxInactiveInterval);
        when(session.getCreationTime()).thenReturn(testCreationTime);
        when(session.getLastAccessedTime()).thenReturn(testLastAccessTime);

        // Act
        ResponseEntity<Map<String, Object>> response = sessionService.getCurrentUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> sessionData = response.getBody();
        assertEquals(testSessionId, sessionData.get("sessionId"));
        assertEquals(testUsername, sessionData.get("username"));
        assertEquals(testLoginTime, sessionData.get("loginTime"));
        assertEquals(testMaxInactiveInterval, sessionData.get("maxInactiveInterval"));
        assertEquals(new Date(testCreationTime), sessionData.get("creationTime"));
        assertEquals(new Date(testLastAccessTime), sessionData.get("lastAccessedTime"));

        verify(request).getSession(false);
    }

    @Test
    void testGetCurrentUser_WithoutActiveSession() {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = sessionService.getCurrentUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No hay sesión activa", response.getBody().get("error"));

        verify(request).getSession(false);
    }

    @Test
    void testGetCurrentUser_WithSessionButNoUsername() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);
        when(session.getAttribute("username")).thenReturn(null);
        when(session.getAttribute("loginTime")).thenReturn(null);
        when(session.getMaxInactiveInterval()).thenReturn(testMaxInactiveInterval);
        when(session.getCreationTime()).thenReturn(testCreationTime);
        when(session.getLastAccessedTime()).thenReturn(testLastAccessTime);

        // Act
        ResponseEntity<Map<String, Object>> response = sessionService.getCurrentUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> sessionData = response.getBody();
        assertEquals(testSessionId, sessionData.get("sessionId"));
        assertNull(sessionData.get("username"));
        assertNull(sessionData.get("loginTime"));
    }

    @Test
    void testLogout_WithActiveSession() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);

        // Act
        ResponseEntity<String> response = sessionService.logout(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sesión cerrada correctamente", response.getBody());

        verify(request).getSession(false);
        verify(session).invalidate();
    }

    @Test
    void testLogout_WithoutActiveSession() {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        ResponseEntity<String> response = sessionService.logout(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No había sesión activa", response.getBody());

        verify(request).getSession(false);
        verify(session, never()).invalidate();
    }

    @Test
    void testGetSessionCount() {
        // Act
        ResponseEntity<String> response = sessionService.getSessionCount();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Spring Session Find by Index"));
    }

    @Test
    void testLogin_SessionAttributesSetCorrectly() {
        // Arrange
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);

        // Act
        sessionService.login(testUsername, request);

        // Assert
        verify(session).setAttribute("username", testUsername);
        verify(session).setAttribute(eq("loginTime"), argThat(loginTime -> {
            // Verificar que el tiempo de login sea una fecha válida
            try {
                LocalDateTime.parse((String) loginTime);
                return true;
            } catch (Exception e) {
                return false;
            }
        }));
    }

    @Test
    void testGetCurrentUser_SessionDataIntegrity() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(testSessionId);
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(session.getAttribute("loginTime")).thenReturn(testLoginTime);
        when(session.getMaxInactiveInterval()).thenReturn(testMaxInactiveInterval);
        when(session.getCreationTime()).thenReturn(testCreationTime);
        when(session.getLastAccessedTime()).thenReturn(testLastAccessTime);

        // Act
        ResponseEntity<Map<String, Object>> response = sessionService.getCurrentUser(request);

        // Assert
        Map<String, Object> sessionData = response.getBody();
        assertNotNull(sessionData);
        assertEquals(6, sessionData.size()); // Verificar que se incluyen todos los campos esperados

        // Verificar tipos de datos
        assertTrue(sessionData.get("sessionId") instanceof String);
        assertTrue(sessionData.get("username") instanceof String);
        assertTrue(sessionData.get("loginTime") instanceof String);
        assertTrue(sessionData.get("maxInactiveInterval") instanceof Integer);
        assertTrue(sessionData.get("creationTime") instanceof Date);
        assertTrue(sessionData.get("lastAccessedTime") instanceof Date);
    }
}