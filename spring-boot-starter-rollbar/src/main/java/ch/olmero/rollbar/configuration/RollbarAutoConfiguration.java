package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.AbstractRollbarNotificationService;
import ch.olmero.rollbar.DefaultRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RollbarAutoConfiguration {

	@RequiredArgsConstructor
	@Configuration
	@ConditionalOnProperty(prefix = "com.rollbar", name = "enabled", havingValue = "true", matchIfMissing = true)
	@EnableConfigurationProperties(RollbarProperties.class)
	static class RollbarConfiguration {
		private final RollbarProperties rollbarProperties;

		@Bean
		public Rollbar rollbar() {
			Config config = ConfigBuilder
				.withAccessToken(this.rollbarProperties.getAccessToken())
				.environment(this.rollbarProperties.getEnvironment())
				.codeVersion(this.rollbarProperties.getCodeVersion())
				.build();

			return Rollbar.init(config);
		}

		@Bean
		public RollbarNotificationService notificationService() {
			return new DefaultRollbarNotificationService(rollbar());
		}
	}

	@Configuration
	@ConditionalOnMissingBean(RollbarNotificationService.class)
	static class NoOpConfiguration {
		@Bean
		public RollbarNotificationService notificationService() {
			return new NoOpRollbarNotificationService();
		}

		static class NoOpRollbarNotificationService extends AbstractRollbarNotificationService {
			@Override
			public void log(String message, Throwable throwable, RollbarNotificationService.Level level) {}
		}
	}
}
