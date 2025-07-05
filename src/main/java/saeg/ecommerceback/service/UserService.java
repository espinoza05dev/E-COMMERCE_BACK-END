package saeg.ecommerceback.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import saeg.ecommerceback.model.User;
import saeg.ecommerceback.repository.IUserRepository;


@Service
@CacheConfig(cacheNames = "users ")
public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(key = "#id")
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Cacheable(key = "#email")
    public User getUserByEmail(String email) {
        return (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(key = "#id")
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    public void clearAllUsersCache() {
        // Limpia todo el cache de usuarios
    }
}
