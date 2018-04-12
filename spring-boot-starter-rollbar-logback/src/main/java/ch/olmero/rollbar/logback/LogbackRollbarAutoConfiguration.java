package ch.olmero.rollbar.logback;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class LogbackRollbarAutoConfiguration {
	private final Environment environment;

	@Bean
	public LogbackLoggingBeanPostProcessor logbackLoggingAbstractBeanFactory() {
		return new LogbackLoggingBeanPostProcessor(new LogbackLoggingConfigurer(environment));
	}
}
