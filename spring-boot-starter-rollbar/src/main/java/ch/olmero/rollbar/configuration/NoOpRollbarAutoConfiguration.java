package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.AbstractRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(RollbarAutoConfiguration.class)
@RequiredArgsConstructor
@Configuration
public class NoOpRollbarAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public RollbarNotificationService notificationService() {
		return new NoopRollbarNotificationService();
	}

	static class NoopRollbarNotificationService extends AbstractRollbarNotificationService {
		@Override
		public void log(String message, Throwable throwable, Level level) {
		}
	}
}
