package UMC.career_mate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class CareerMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerMateApplication.class, args);
	}

}
