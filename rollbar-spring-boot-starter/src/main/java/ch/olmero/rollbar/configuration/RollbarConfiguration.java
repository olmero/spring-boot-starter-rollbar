package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.DefaultRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(RollbarProperties.class)
@ConditionalOnProperty(prefix = "com.rollbar", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RollbarConfiguration {
	private final RollbarProperties rollbarProperties;
	private final ObjectProvider<GitProperties> gitProperties;

	@Bean
	@ConditionalOnMissingBean
	public Config rollbarConfiguration() {
		ConfigBuilder configBuilder = ConfigBuilder
			.withAccessToken(this.rollbarProperties.getAccessToken())
			.environment(this.rollbarProperties.getEnvironment());

		setCodeVersion(configBuilder, this.gitProperties);

		return configBuilder.build();
	}

	@Bean
	public Rollbar rollbar() {
		return Rollbar.init(rollbarConfiguration());
	}

	@Bean
	public RollbarNotificationService notificationService() {
		return new DefaultRollbarNotificationService(rollbar());
	}

	private void setCodeVersion(ConfigBuilder configBuilder, ObjectProvider<GitProperties> gitProperties) {
		if (this.rollbarProperties.getCodeVersion() == null) {
			gitProperties.ifAvailable(properties -> configBuilder.codeVersion(properties.getCommitId()));
		} else {
			configBuilder.codeVersion(this.rollbarProperties.getCodeVersion());
		}
	}
}
