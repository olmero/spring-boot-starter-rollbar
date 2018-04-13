package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.NoOpRollbarNotificationService;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class NoOpRollbarAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(RollbarAutoConfiguration.class, NoOpRollbarAutoConfiguration.class));

	@Test
	public void disableRollbar() {
		this.contextRunner
			.withPropertyValues("com.rollbar.enabled:false")
			.run(context -> assertThat(context).hasSingleBean(NoOpRollbarNotificationService.NoOpRollbarNotificationService.class));
	}
}