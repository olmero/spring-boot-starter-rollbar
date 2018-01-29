package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.DefaultRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@AutoConfigureBefore(NoopRollbarAutoConfiguration.class)
@EnableConfigurationProperties(RollbarProperties.class)
@ConditionalOnProperty(prefix = "com.rollbar", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RollbarAutoConfiguration {
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
