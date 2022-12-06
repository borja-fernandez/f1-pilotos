package com.f1.pilots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.f1.pilots.query.repository"})
@EnableJpaRepositories(basePackages = {"com.f1.pilots.command.repository", "com.f1.pilots.sync.repository"})
public class PilotsApplication {

	public static final String PILOT_QUERY_PATH = "/pilot/query";
	public static final String PILOT_COMMAND_PATH = "/pilot/command";
	public static final String PILOT_SYNC_PATH = "/pilot/sync";

	public static void main(String[] args) {
		SpringApplication.run(PilotsApplication.class, args);
	}

}
