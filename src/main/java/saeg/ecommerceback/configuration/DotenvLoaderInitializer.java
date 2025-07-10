package saeg.ecommerceback.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotenvLoaderInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public DotenvLoaderInitializer(){
        Dotenv.load();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.load();
        //DataBase
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        //REDIS
        System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
        System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
        System.setProperty("REDIS_PASSWORD", dotenv.get("REDIS_PASSWORD"));
        System.setProperty("REDIS_USER", dotenv.get("REDIS_USER"));
        System.setProperty("REDIS_DB", dotenv.get("REDIS_DB"));


        //jwt
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        System.setProperty("JWT_DEV_PWD", dotenv.get("JWT_DEV_PWD"));

        //stripe
        System.setProperty("STRIPE_API_KEY", dotenv.get("STRIPE_API_KEY"));

        //sendgrid
        System.setProperty("SENDGRID_API_KEY", dotenv.get("SENDGRID_API_KEY"));
    }
}
