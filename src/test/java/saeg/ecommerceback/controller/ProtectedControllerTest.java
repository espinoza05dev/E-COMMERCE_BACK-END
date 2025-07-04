package saeg.ecommerceback.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProtectedControllerTest {

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private ProtectedController protectedController;

    @Test
    void testAdminEndpoint() {
        // Given
        when(authentication.getName()).thenReturn("admin");

        // When
        ResponseEntity<String> response = protectedController.adminEndpoint(authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello Admin: admin", response.getBody());
        verify(authentication).getName();
    }

    @Test
    void testUserEndpoint() {
        // Given
        when(authentication.getName()).thenReturn("testuser");

        // When
        ResponseEntity<String> response = protectedController.userEndpoint(authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello User: testuser", response.getBody());
        verify(authentication).getName();
    }

    @Test
    void testGetUserInfoWithSession() {
        // Given
        long creationTime = System.currentTimeMillis() - 3600000; // 1 hour ago
        long lastAccessTime = System.currentTimeMillis() - 300000; // 5 minutes ago

        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("session123");
        when(httpSession.getCreationTime()).thenReturn(creationTime);
        when(httpSession.getLastAccessedTime()).thenReturn(lastAccessTime);

        // When
        ResponseEntity<Map<String, Object>> response = protectedController.getUserInfo(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("testuser", body.get("username"));
        assertEquals(authentication.getAuthorities(), body.get("authorities"));
        assertEquals("session123", body.get("sessionId"));

        assertTrue(body.get("creationTime") instanceof Date);
        assertTrue(body.get("lastAccessedTime") instanceof Date);

        Date creationDate = (Date) body.get("creationTime");
        Date lastAccessDate = (Date) body.get("lastAccessedTime");

        assertEquals(creationTime, creationDate.getTime());
        assertEquals(lastAccessTime, lastAccessDate.getTime());

        verify(authentication).getName();
        verify(authentication).getAuthorities();
        verify(httpServletRequest).getSession(false);
        verify(httpSession).getId();
        verify(httpSession).getCreationTime();
        verify(httpSession).getLastAccessedTime();
    }

    @Test
    void testGetUserInfoWithoutSession() {
        // Given
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection)List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(false)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = protectedController.getUserInfo(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("testuser", body.get("username"));
        assertEquals(authentication.getAuthorities(), body.get("authorities"));
        assertNull(body.get("sessionId"));
        assertNull(body.get("creationTime"));
        assertNull(body.get("lastAccessedTime"));

        verify(authentication).getName();
        verify(authentication).getAuthorities();
        verify(httpServletRequest).getSession(false);
    }

    @Test
    void testGetUserInfoWithMultipleRoles() {
        // Given
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(authentication.getName()).thenReturn("adminuser");
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("session456");
        when(httpSession.getCreationTime()).thenReturn(System.currentTimeMillis());
        when(httpSession.getLastAccessedTime()).thenReturn(System.currentTimeMillis());

        // When
        ResponseEntity<Map<String, Object>> response = protectedController.getUserInfo(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("adminuser", body.get("username"));
        assertEquals(authorities, body.get("authorities"));
        assertEquals("session456", body.get("sessionId"));
    }

    @Test
    void testGetUserInfoSessionTimestamps() {
        // Given
        long now = System.currentTimeMillis();
        long oneHourAgo = now - 3600000;

        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn((Collection)List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("session789");
        when(httpSession.getCreationTime()).thenReturn(oneHourAgo);
        when(httpSession.getLastAccessedTime()).thenReturn(now);

        // When
        ResponseEntity<Map<String, Object>> response = protectedController.getUserInfo(authentication, httpServletRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();

        Date creationTime = (Date) body.get("creationTime");
        Date lastAccessTime = (Date) body.get("lastAccessedTime");

        assertEquals(oneHourAgo, creationTime.getTime());
        assertEquals(now, lastAccessTime.getTime());
        assertTrue(lastAccessTime.after(creationTime));
    }
}