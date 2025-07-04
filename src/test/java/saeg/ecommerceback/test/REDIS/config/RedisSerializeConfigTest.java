package saeg.ecommerceback.test.REDIS.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import saeg.ecommerceback.configuration.RedisSerializeConfig;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedisSerializeConfigTest {

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @InjectMocks
    private RedisSerializeConfig redisSerializeConfig;

    @Test
    void testStringRedisTemplate_ShouldConfigureTemplateCorrectly(){
        //Act
        RedisTemplate<String,String> redisTemplate = redisSerializeConfig.stringRedisTemplate(redisConnectionFactory);

        //Assert
        assertNotNull(redisTemplate);
        assertEquals(redisConnectionFactory, redisTemplate.getConnectionFactory());

        //Verify Serializers
        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisTemplate);
        assertTrue(redisTemplate.getValueSerializer() instanceof StringRedisTemplate);
        assertTrue(redisTemplate.getHashKeySerializer() instanceof StringRedisTemplate);
        assertTrue(redisTemplate.getHashValueSerializer() instanceof StringRedisTemplate);
    }

    @Test
    void testObjectRedisTemplate_ShouldConfigureTemplateCorrectly() {
        // Act
        RedisTemplate<String, Object> template = redisSerializeConfig.objectRedisTemplate(redisConnectionFactory);

        // Assert
        assertNotNull(template);
        assertEquals(redisConnectionFactory, template.getConnectionFactory());

        // Verify serializers
        assertTrue(template.getKeySerializer() instanceof StringRedisSerializer);
        assertTrue(template.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
        assertTrue(template.getHashKeySerializer() instanceof StringRedisSerializer);
        assertTrue(template.getHashValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
    }

    @Test
    void testStringRedisTemplate_ShouldHandleNullConnectionFactory() {
        // Act
        RedisTemplate<String, String> template = redisSerializeConfig.stringRedisTemplate(null);

        // Assert
        assertNotNull(template);
        assertNull(template.getConnectionFactory());
    }

    @Test
    void testObjectRedisTemplate_ShouldHandleNullConnectionFactory() {
        // Act
        RedisTemplate<String, Object> template = redisSerializeConfig.objectRedisTemplate(null);

        // Assert
        assertNotNull(template);
        assertNull(template.getConnectionFactory());
    }

    @Test
    void testStringRedisTemplate_ShouldSetAllSerializers() {
        // Act
        RedisTemplate<String, String> template = redisSerializeConfig.stringRedisTemplate(redisConnectionFactory);

        // Assert
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
        assertNotNull(template.getHashKeySerializer());
        assertNotNull(template.getHashValueSerializer());
    }

    @Test
    void testObjectRedisTemplate_ShouldSetAllSerializers() {
        // Act
        RedisTemplate<String, Object> template = redisSerializeConfig.objectRedisTemplate(redisConnectionFactory);

        // Assert
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
        assertNotNull(template.getHashKeySerializer());
        assertNotNull(template.getHashValueSerializer());
    }

    @Test
    void testStringRedisTemplate_ShouldBeConfiguredAfterPropertiesSet() {
        // Act
        RedisTemplate<String, String> template = redisSerializeConfig.stringRedisTemplate(redisConnectionFactory);

        // Assert
        // Verify that afterPropertiesSet was called by checking template is properly initialized
        assertNotNull(template);
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
    }

    @Test
    void testObjectRedisTemplate_ShouldBeConfiguredAfterPropertiesSet() {
        // Act
        RedisTemplate<String, Object> template = redisSerializeConfig.objectRedisTemplate(redisConnectionFactory);

        // Assert
        // Verify that afterPropertiesSet was called by checking template is properly initialized
        assertNotNull(template);
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
    }
}