package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.RollbarNotificationService;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

public class NoOpRollbarAutoConfigurationTest {
	@Test
	public void disableRollbar() {
		try (ConfigurableApplicationContext ctxt = init("com.rollbar.enabled:false")) {
			assertThat(ctxt.getBean(RollbarNotificationService.class)).isInstanceOf(RollbarAutoConfiguration.NoOpConfiguration.NoOpRollbarNotificationService.class);
		}
	}

	protected ConfigurableApplicationContext init(String... pairs) {
		return new SpringApplicationBuilder().sources(Config.class)
			.properties(pairs).run();
	}

	@Configuration
	@EnableAutoConfiguration
	protected static class Config {
	}
}