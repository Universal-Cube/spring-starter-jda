package org.universalcube.spring_starter_discord;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.universalcube.spring_starter_discord.configuration.JdaAutoConfiguration;

@SpringBootTest(args = {"--spring.profiles.active=test"}, classes = {JdaAutoConfiguration.class})
@ContextConfiguration
public class JdaStarterTests {

	@Test
	void contextLoads() {
	}
}
