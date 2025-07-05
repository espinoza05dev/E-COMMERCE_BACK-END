package saeg.ecommerceback.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import saeg.ecommerceback.model.User;
import saeg.ecommerceback.repository.IUserRepository;
import saeg.ecommerceback.security.UserPrincipal;

public class CustomUserDetailsService  implements UserDetailsService {

    private static final IUserRepository userRepository = null;
    public CustomUserDetailsService(IUserRepository userRepository) {
        userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     User user = userRepository.findByEmail(username).orElseThrow(()->  new UsernameNotFoundException("Usuario no encontrado: " + username));

     return new UserPrincipal(user);
    }

    public static UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + id));

        return new UserPrincipal(user);
    }
}
