package saeg.ecommerceback.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

@RestController
@RequestMapping("/api/redis-test")
public class ControllerRedis {
    private final RedisTemplate<String, String> redisTemplate;
    public ControllerRedis(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //connection to redis
    @GetMapping("/ping")
    public ResponseEntity<String> pingRedis() {
        try {
            String res = redisTemplate.getConnectionFactory().getConnection().ping();
            return ResponseEntity.ok("conectado correctamente " + res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al conectar a Redis"+e.getMessage());
        }
    }

    //save a value
    @PostMapping("/set/{key}/{value}")
    public ResponseEntity<String> setValue(@PathVariable String key, @PathVariable String value) {
        try{
            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10));
            return ResponseEntity.ok().body("OK");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar el valor"+e.getMessage());
        }
    }

    // Obtener un valor de Redis
    @GetMapping("/get/{key}")
    public ResponseEntity<String> getValue(@PathVariable String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                return ResponseEntity.ok("Valor encontrado: " + key + " = " + value);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo de Redis: " + e.getMessage());
        }
    }

    // Listar todas las claves
    @GetMapping("/keys")
    public ResponseEntity<Set<String>> getAllKeys() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            return ResponseEntity.ok(keys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptySet());
        }
    }

    // Eliminar una clave
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<String> deleteKey(@PathVariable String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                return ResponseEntity.ok("Clave eliminada: " + key);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error eliminando de Redis: " + e.getMessage());
        }
    }

    // Probar cache con TTL
    @PostMapping("/cache/{key}/{value}/{seconds}")
    public ResponseEntity<String> cacheWithTTL(@PathVariable String key,
                                               @PathVariable String value,
                                               @PathVariable long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
            return ResponseEntity.ok(String.format("Valor cacheado por %d segundos: %s = %s",
                    seconds, key, value));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cacheando en Redis: " + e.getMessage());
        }
    }

    // Obtener información del servidor Redis
    @GetMapping("/info")
    public ResponseEntity<String> getRedisInfo() {
        try {
            Properties info = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .info();
            return ResponseEntity.ok("Info de Redis: " + info.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo info de Redis: " + e.getMessage());
        }
    }
}
