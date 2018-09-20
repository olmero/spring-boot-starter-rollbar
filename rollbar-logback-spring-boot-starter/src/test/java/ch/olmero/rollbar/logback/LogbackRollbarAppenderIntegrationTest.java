package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.olmero.rollbar.configuration.RollbarAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
class LogbackRollbarAppenderIntegrationTest {
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@AfterEach
	void after() {
		this.context.close();
	}

	@Test
	void overrideRootLevel() {
		TestPropertyValues.of("com.rollbar.logging.level.root=INFO",
				"com.rollbar.access-token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				"com.rollbar.environment=test").applyTo(this.context);

		context.register(RollbarAutoConfiguration.class, LogbackRollbarAutoConfiguration.class, MockNotificationServiceConfiguration.class);
		context.refresh();

		log.info("some info");

		RollbarNotificationService rollbarNotificationService = this.context.getBean(RollbarNotificationService.class);
		verify(rollbarNotificationService).log("some info", null, RollbarNotificationService.Level.INFO);
	}

	@Configuration
	static class MockNotificationServiceConfiguration {
		@Bean
		public RollbarNotificationService notificationService() {
			return mock(RollbarNotificationService.class);
		}

	}
}