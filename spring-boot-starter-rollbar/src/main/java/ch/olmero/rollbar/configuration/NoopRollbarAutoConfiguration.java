package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.AbstractRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnMissingBean(RollbarNotificationService.class)
public class NoopRollbarAutoConfiguration {
	@Bean
	public RollbarNotificationService notificationService() {
		return new NoopRollbarNotificationService();
	}

	static class NoopRollbarNotificationService extends AbstractRollbarNotificationService {
		@Override
		public void log(String message, Throwable throwable, Level level) {}
	}
}
