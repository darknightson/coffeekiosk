package sample.coffeekiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CoffeekioskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeekioskApplication.class, args);
	}

}
