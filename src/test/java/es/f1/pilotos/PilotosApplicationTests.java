package es.f1.pilotos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=local", classes = PilotosApplication.class)
@TestPropertySource("classpath.application.yml")
class PilotosApplicationTests {

	@InjectMocks
	PilotosApplication pilotosApplication;

	@Test
	void contextLoads() {
	}

}
