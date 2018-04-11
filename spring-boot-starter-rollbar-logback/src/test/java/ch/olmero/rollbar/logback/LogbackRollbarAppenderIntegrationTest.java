package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.olmero.rollbar.configuration.RollbarAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
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
		TestPropertyValues.of("com.rollbar.logging.level.root=INFO",
				"com.rollbar.access-token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				"com.rollbar.environment=test").applyTo(this.context);

		context.register(RollbarAutoConfiguration.class, MockNotificationServiceConfiguration.class);
		context.refresh();

		this.initializer.onApplicationEvent(new ContextRefreshedEvent(context));

		RollbarNotificationService rollbarNotificationService = this.context.getBean(RollbarNotificationService.class);

		log.info("some info");

		verify(rollbarNotificationService).log("some info", null, RollbarNotificationService.Level.INFO);
	}

	@Configuration
	@MockBean(RollbarNotificationService.class)
	static class MockNotificationServiceConfiguration {
	}
}