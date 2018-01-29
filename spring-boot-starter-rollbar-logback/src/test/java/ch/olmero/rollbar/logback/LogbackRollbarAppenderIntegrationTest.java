package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.olmero.rollbar.configuration.RollbarAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@Slf4j
public class LogbackRollbarAppenderIntegrationTest {
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
	private LogbackLoggingApplicationListener initializer = new LogbackLoggingApplicationListener();

	@Before
	public void setup() {
		MockitoPostProcessor.register(this.context);
	}

	@After
	public void after() {
		this.context.close();
	}

	@Test
	public void shouldBeRegistered() {
		assertThat(SpringFactoriesLoader.loadFactories(ApplicationListener.class, null))
			.hasAtLeastOneElementOfType(LogbackLoggingApplicationListener.class);
	}

	@Test
	public void overrideRootLevel() {
		EnvironmentTestUtils.addEnvironment(this.context,
			"com.rollbar.logging.level.root=INFO",
			"com.rollbar.access-token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
			"com.rollbar.environment=test");

		context.register(RollbarAutoConfiguration.class, MockNotificationServiceConfiguration.class);
		context.refresh();

		this.initializer.onApplicationEvent(new ApplicationReadyEvent(new SpringApplication(), null, context));

		RollbarNotificationService rollbarNotificationService = this.context.getBean(RollbarNotificationService.class);

		log.info("some info");

		verify(rollbarNotificationService).log("some info", null, RollbarNotificationService.Level.INFO);
	}

	@Configuration
	@MockBean(RollbarNotificationService.class)
	static class MockNotificationServiceConfiguration {
	}
}