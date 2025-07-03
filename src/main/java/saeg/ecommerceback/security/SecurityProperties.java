package saeg.ecommerceback.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security")
@Validated
public class SecurityProperties {
    @NotBlank
    private String jwtSecret;
    @NotBlank
    private Long jwtExpiration;
    @NotBlank
    private String stipeApiKey;
    @NotBlank
    private String sendgripApiKey;

}
