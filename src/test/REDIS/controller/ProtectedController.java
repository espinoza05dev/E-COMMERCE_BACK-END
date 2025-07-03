package saeg.ecommerceback.test.REDIS.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminEndpoint(Authentication auth) {
        return ResponseEntity.ok("Hello Admin: " + auth.getName());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> userEndpoint(Authentication auth) {
        return ResponseEntity.ok("Hello User: " + auth.getName());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication auth,
                                                           HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        info.put("username", auth.getName());
        info.put("authorities", auth.getAuthorities());

        HttpSession session = request.getSession(false);
        if (session != null) {
            info.put("sessionId", session.getId());
            info.put("creationTime", new Date(session.getCreationTime()));
            info.put("lastAccessedTime", new Date(session.getLastAccessedTime()));
        }

        return ResponseEntity.ok(info);
    }
}
