package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@RequiredArgsConstructor
public class LogbackLoggingBeanPostProcessor implements BeanPostProcessor {

	private final LogbackLoggingConfigurer logbackLoggingConfigurer;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RollbarNotificationService) {
			logbackLoggingConfigurer.configure((RollbarNotificationService)bean);
		}

		return bean;
	}

}
