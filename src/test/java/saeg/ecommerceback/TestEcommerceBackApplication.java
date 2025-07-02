package saeg.ecommerceback;

import org.springframework.boot.SpringApplication;

public class TestEcommerceBackApplication {

    public static void main(String[] args) {
        SpringApplication.from(EcommerceBackApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
