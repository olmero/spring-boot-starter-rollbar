package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.NoOpRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoOpRollbarConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public RollbarNotificationService notificationService() {
		return new NoOpRollbarNotificationService();
	}

}
