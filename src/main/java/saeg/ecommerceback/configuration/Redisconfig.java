package saeg.ecommerceback.configuration;

import io.jsonwebtoken.lang.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class Redisconfig {
    private final Environment environment;

    public Redisconfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${spring.data.redis.host}") String host,
                                                         @Value("${spring.data.redis.port}") int port,
                                                         @Value("${spring.data.redis.password}") String password) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
        if (password != null && !password.isEmpty()) {
            factory.setPassword(password);
        }

        if (isProductionProfile()) {
            factory.setUseSsl(true);
        }

        return factory;
    }

    private boolean isProductionProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }
}
