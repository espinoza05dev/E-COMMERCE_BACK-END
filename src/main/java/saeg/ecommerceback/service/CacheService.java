package saeg.ecommerceback.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
public class CacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public CacheService(RedisTemplate<String, Object> redisTemplate,
                        RedisTemplate<String, String> stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // Cache de objetos
    public void cacheObject(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public <T> T getCachedObject(String key, Class<T> type) {
        Object cached = redisTemplate.opsForValue().get(key);
        return cached != null ? (T) cached : null;
    }

    // Cache de strings
    public void cacheString(String key, String value, Duration ttl) {
        stringRedisTemplate.opsForValue().set(key, value, ttl);
    }

    public String getCachedString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // Lists
    public void addToList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // Sets
    public void addToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // Hash Maps
    public void putHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    // Utilidades
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void setExpire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }
}
