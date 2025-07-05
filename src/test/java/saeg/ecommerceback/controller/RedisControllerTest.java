package saeg.ecommerceback.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisControllerTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Mock
    private RedisConnection redisConnection;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisController redisController;

    @Test
    void testPingRedisSuccess() {
        // Given
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("PONG");

        // When
        ResponseEntity<String> response = redisController.pingRedis();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("conectado correctamente"));
        assertTrue(response.getBody().contains("PONG"));
        verify(redisConnection).ping();
    }

    @Test
    void testPingRedisFailure() {
        // Given
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenThrow(new RuntimeException("Connection failed"));

        // When
        ResponseEntity<String> response = redisController.pingRedis();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error al conectar a Redis"));
        assertTrue(response.getBody().contains("Connection failed"));
    }

    @Test
    void testSetValueSuccess() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        ResponseEntity<String> response = redisController.setValue("testkey", "testvalue");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OK", response.getBody());
        verify(valueOperations).set("testkey", "testvalue", Duration.ofMinutes(10));
    }

    @Test
    void testSetValueFailure() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new RuntimeException("Redis error")).when(valueOperations)
                .set(any(), any(), any(Duration.class));

        // When
        ResponseEntity<String> response = redisController.setValue("testkey", "testvalue");

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error al agregar el valor"));
        assertTrue(response.getBody().contains("Redis error"));
    }

    @Test
    void testGetValueSuccess() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("testkey")).thenReturn("testvalue");

        // When
        ResponseEntity<String> response = redisController.getValue("testkey");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Valor encontrado"));
        assertTrue(response.getBody().contains("testkey"));
        assertTrue(response.getBody().contains("testvalue"));
    }

    @Test
    void testGetValueNotFound() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("nonexistent")).thenReturn(null);

        // When
        ResponseEntity<String> response = redisController.getValue("nonexistent");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetValueFailure() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("testkey")).thenThrow(new RuntimeException("Redis error"));

        // When
        ResponseEntity<String> response = redisController.getValue("testkey");

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error obteniendo de Redis"));
        assertTrue(response.getBody().contains("Redis error"));
    }

    @Test
    void testGetAllKeysSuccess() {
        // Given
        Set<String> keys = Set.of("key1", "key2", "key3");
        when(redisTemplate.keys("*")).thenReturn(keys);

        // When
        ResponseEntity<Set<String>> response = redisController.getAllKeys();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(keys, response.getBody());
        verify(redisTemplate).keys("*");
    }

    @Test
    void testGetAllKeysFailure() {
        // Given
        when(redisTemplate.keys("*")).thenThrow(new RuntimeException("Redis error"));

        // When
        ResponseEntity<Set<String>> response = redisController.getAllKeys();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testDeleteKeySuccess() {
        // Given
        when(redisTemplate.delete("testkey")).thenReturn(true);

        // When
        ResponseEntity<String> response = redisController.deleteKey("testkey");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Clave eliminada"));
        assertTrue(response.getBody().contains("testkey"));
        verify(redisTemplate).delete("testkey");
    }

    @Test
    void testDeleteKeyNotFound() {
        // Given
        when(redisTemplate.delete("nonexistent")).thenReturn(false);

        // When
        ResponseEntity<String> response = redisController.deleteKey("nonexistent");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteKeyFailure() {
        // Given
        when(redisTemplate.delete("testkey")).thenThrow(new RuntimeException("Redis error"));

        // When
        ResponseEntity<String> response = redisController.deleteKey("testkey");

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error eliminando de Redis"));
        assertTrue(response.getBody().contains("Redis error"));
    }

    @Test
    void testCacheWithTTLSuccess() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        ResponseEntity<String> response = redisController.cacheWithTTL("testkey", "testvalue", 30);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Valor cacheado por 30 segundos"));
        assertTrue(response.getBody().contains("testkey"));
        assertTrue(response.getBody().contains("testvalue"));
        verify(valueOperations).set("testkey", "testvalue", Duration.ofSeconds(30));
    }

    @Test
    void testCacheWithTTLFailure() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new RuntimeException("Redis error")).when(valueOperations)
                .set(any(), any(), any(Duration.class));

        // When
        ResponseEntity<String> response = redisController.cacheWithTTL("testkey", "testvalue", 30);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error cacheando en Redis"));
        assertTrue(response.getBody().contains("Redis error"));
    }

    @Test
    void testGetRedisInfoSuccess() {
        // Given
        Properties properties = new Properties();
        properties.setProperty("redis_version", "6.2.6");
        properties.setProperty("used_memory", "1024");

        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.info()).thenReturn(properties);

        // When
        ResponseEntity<String> response = redisController.getRedisInfo();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Info de Redis"));
        verify(redisConnection).info();
    }

    @Test
    void testGetRedisInfoFailure() {
        // Given
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.info()).thenThrow(new RuntimeException("Connection error"));

        // When
        ResponseEntity<String> response = redisController.getRedisInfo();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error obteniendo info de Redis"));
        assertTrue(response.getBody().contains("Connection error"));
    }
}