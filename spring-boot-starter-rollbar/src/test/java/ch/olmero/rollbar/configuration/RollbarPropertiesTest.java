package ch.olmero.rollbar.configuration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Throwables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.BindException;

import static org.assertj.core.api.Assertions.assertThat;

public class RollbarPropertiesTest {
	@Test
	public void missingRequiredProperties() {
		try (AnnotationConfigApplicationContext context = createContext()) {
			Assertions.fail("Expected BindException to be thrown");
		} catch (Exception e) {
			Throwable rootCause = Throwables.getRootCause(e);
			assertThat(rootCause).isInstanceOf(BindException.class);

			BindException bindException = (BindException)rootCause;

			assertThat(bindException.getBindingResult().getFieldErrors()).extracting("field")
				.containsOnly("accessToken", "environment");
		}
	}

	@Test
	public void shouldBeValid() {
		try (AnnotationConfigApplicationContext context = createContext("com.rollbar.accessToken:abc", "com.rollbar.environment:test")) {
		}
	}

	@Test
	public void useCommitIdAsCodeVersion() {
		try (AnnotationConfigApplicationContext context = createContext("com.rollbar.accessToken:abc", "com.rollbar.environment:test")) {
			RollbarProperties rollbarProperties = context.getBean(RollbarProperties.class);
			assertThat(rollbarProperties.getCodeVersion()).isEqualTo("2b99962a85ee6cb7d48f05a65a5d529398d3e8be");
		}
	}

	private AnnotationConfigApplicationContext createContext(String... pairs) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

		EnvironmentTestUtils.addEnvironment(context, pairs);

		context.register(RollbarPropertiesConfiguration.class);
		context.refresh();

		return context;
	}

	@RequiredArgsConstructor
	@Configuration
	@EnableConfigurationProperties(RollbarProperties.class)
	@Import(ProjectInfoAutoConfiguration.class)
	public static class RollbarPropertiesConfiguration {
		private final RollbarProperties rollbarProperties;
	}
}