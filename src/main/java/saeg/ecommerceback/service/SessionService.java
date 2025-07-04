package saeg.ecommerceback.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session-test")
public class SessionService {

        // Probar sesiones con Redis
        @PostMapping("/login/{username}")
        public ResponseEntity<String> login(@PathVariable String username,
                                            HttpServletRequest request) {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("loginTime", LocalDateTime.now().toString());

            return ResponseEntity.ok("Usuario " + username + " logueado. Session ID: " +
                    session.getId());
        }

        @GetMapping("/user")
        public ResponseEntity<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
            HttpSession session = request.getSession(false);

            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No hay sesión activa"));
            }

            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("sessionId", session.getId());
            sessionData.put("username", session.getAttribute("username"));
            sessionData.put("loginTime", session.getAttribute("loginTime"));
            sessionData.put("maxInactiveInterval", session.getMaxInactiveInterval());
            sessionData.put("creationTime", new Date(session.getCreationTime()));
            sessionData.put("lastAccessedTime", new Date(session.getLastAccessedTime()));

            return ResponseEntity.ok(sessionData);
        }

        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletRequest request) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
                return ResponseEntity.ok("Sesión cerrada correctamente");
            }

            return ResponseEntity.ok("No había sesión activa");
        }

        @GetMapping("/sessions/count")
        public ResponseEntity<String> getSessionCount() {
            // En un entorno real, podrías usar Spring Session para contar sesiones activas
            return ResponseEntity.ok("Para contar sesiones activas necesitas configurar Spring Session Find by Index");
        }
}
