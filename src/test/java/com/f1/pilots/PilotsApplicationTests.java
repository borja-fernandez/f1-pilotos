package com.f1.pilots;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=local", classes = PilotsApplication.class)
@TestPropertySource("classpath.application.yml")
class PilotsApplicationTests {

	@InjectMocks
    PilotsApplication pilotsApplication;

	@Test
	void contextLoads() {
	}

}
