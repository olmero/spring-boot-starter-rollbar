package ch.olmero.rollbar.configuration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Throwables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;

import static org.assertj.core.api.Assertions.assertThat;

public class RollbarPropertiesTest {
	private AnnotationConfigApplicationContext context;

	@Before
	public void before() {
		this.context = new AnnotationConfigApplicationContext();
	}

	@After
	public void after() {
		this.context.close();
	}

	@Test
	public void missingRequiredProperties() {
		try {
			this.context.register(RollbarPropertiesConfiguration.class);
			this.context.refresh();

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
		EnvironmentTestUtils.addEnvironment(this.context,
			"com.rollbar.accessToken:abc",
			"com.rollbar.environment:test");

		this.context.register(RollbarPropertiesConfiguration.class);
		this.context.refresh();
	}

	@RequiredArgsConstructor
	@Configuration
	@EnableConfigurationProperties(RollbarProperties.class)
	public static class RollbarPropertiesConfiguration {
		private final RollbarProperties rollbarProperties;
	}
}