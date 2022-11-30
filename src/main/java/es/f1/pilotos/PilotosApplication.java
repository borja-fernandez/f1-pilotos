package es.f1.pilotos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"es.f1.pilotos.query.repository"})
@EnableJpaRepositories(basePackages = {"es.f1.pilotos.command.repository", "es.f1.pilotos.sync.repository"})
public class PilotosApplication {

	public static final String PILOTOS_QUERY_PATH = "/pilotos/consultas";
	public static final String PILOTOS_COMMAND_PATH = "/pilotos/modificaciones";
	public static final String PILOTOS_SYNC_PATH = "/pilotos/sync";

	public static void main(String[] args) {
		SpringApplication.run(PilotosApplication.class, args);
	}

}
