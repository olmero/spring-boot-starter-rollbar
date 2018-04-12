package ch.olmero.rollbar.configuration;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class NoopRollbarAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(RollbarAutoConfiguration.class, NoopRollbarAutoConfiguration.class));

	@Test
	public void disableRollbar() {
		this.contextRunner
			.withPropertyValues("com.rollbar.enabled:false")
			.run(context -> assertThat(context).hasSingleBean(NoopRollbarAutoConfiguration.NoopRollbarNotificationService.class));
	}
}