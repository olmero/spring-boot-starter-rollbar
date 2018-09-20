package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.DefaultRollbarNotificationService;
import ch.olmero.rollbar.NoOpRollbarNotificationService;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import com.rollbar.notifier.config.ConfigProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class RollbarAutoConfigurationTest {
	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(RollbarAutoConfiguration.class))
		.withPropertyValues("com.rollbar.accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
			"com.rollbar.environment=test");

	@Test
	void verifyConfiguration() {
		this.contextRunner
			.run(context -> {
				Rollbar rollbar = context.getBean(Rollbar.class);

				MockConfigProvider configProvider = new MockConfigProvider();
				rollbar.configure(configProvider);

				assertThat(configProvider.config.accessToken()).isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
				assertThat(configProvider.config.environment()).isEqualTo("test");
			});
	}

	@Test
	void verifyNotificationService() {
		this.contextRunner
			.run(context -> assertThat(context).hasSingleBean(DefaultRollbarNotificationService.class));
	}

	private class MockConfigProvider implements ConfigProvider {
		private Config config;

		@Override
		public Config provide(ConfigBuilder builder) {
			return this.config = builder.build();
		}
	}

	@Test
	void disableRollbar() {
		this.contextRunner
			.withPropertyValues("com.rollbar.enabled:false")
			.run(context -> assertThat(context).hasSingleBean(NoOpRollbarNotificationService.class));
	}

	@Test
	void useCodeVersion() {
		this.contextRunner
			.withPropertyValues("com.rollbar.codeVersion=23218add")
			.run(context -> {
				Config rollbarConfig = context.getBean(Config.class);
				assertThat(rollbarConfig.codeVersion()).isEqualTo("23218add");
			});
	}

	@Test
	void useCommitIdFromProjectInfoAsCodeVersion() {
		this.contextRunner
			.withConfiguration(AutoConfigurations.of(ProjectInfoAutoConfiguration.class))
			.run(context -> {
					Config rollbarConfig = context.getBean(Config.class);
					assertThat(rollbarConfig.codeVersion()).isEqualTo("2b99962a85ee6cb7d48f05a65a5d529398d3e8be");
				});
	}
}