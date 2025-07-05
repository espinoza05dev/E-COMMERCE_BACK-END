package saeg.ecommerceback.configuration;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConfigurationValidator {
    @Value("${jwt.secret}")
    private String Jwtsecret;

    @Value("${spring.profiles.active:}")
        private String activeProfile;

    @Value("${DEV_pwd}")
    private String devPwd;

    @EventListener(ApplicationReadyEvent.class)
    public void validateConfiguration() {
        if (activeProfile.equals("prod")) {
            validateProductionConfiguration();
        }
    }

    private void validateProductionConfiguration() {
        List<String> errors = new ArrayList<>();

        if (Jwtsecret.equals(devPwd)) {
            errors.add("jwt secret is using default developer value");
        }

        if (System.getenv("DB_PASSWORD") == null) {
            errors.add("DataBase password environment variable is not set");
        }

        if (System.getenv("DB_USERNAME") == null) {
            errors.add("DataBase username environment variable is not set");
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException("Production configuration errors: " + String.join(", " + errors));
        }
    }
}
