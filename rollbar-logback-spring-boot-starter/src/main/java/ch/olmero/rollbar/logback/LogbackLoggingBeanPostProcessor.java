package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
public class LogbackLoggingBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

	private final LogbackLoggingConfigurer logbackLoggingConfigurer;
	private Environment environment;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RollbarNotificationService) {
			logbackLoggingConfigurer.configure((RollbarNotificationService)bean, environment);
		}

		return bean;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
