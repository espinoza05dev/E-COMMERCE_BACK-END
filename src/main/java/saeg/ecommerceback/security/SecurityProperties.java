package saeg.ecommerceback.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Configuration
@ConfigurationProperties(prefix = "ecommerce.security")
@Validated
public class SecurityProperties {
    @NotBlank
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @NotNull
    @Positive
    @Value("${JWT_EXPIRATION}")
    private Long jwtExpiration;

//    @NotBlank
//    private String stripeApiKey;
//
//    @NotBlank
//    private String sendgridApiKey;

}
