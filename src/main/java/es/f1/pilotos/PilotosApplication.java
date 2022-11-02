package es.f1.pilotos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PilotosApplication {

	public static final String PILOTOS_QUERY_PATH = "/pilotos/consultas";
	public static final String PILOTOS_COMMAND_PATH = "/pilotos/modificaciones";
	public static void main(String[] args) {
		SpringApplication.run(PilotosApplication.class, args);
	}

}
