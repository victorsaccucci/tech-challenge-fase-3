package serviceschedule_api.fiap.serviceschedule_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceScheduleApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceScheduleApiApplication.class, args);
	}

}
