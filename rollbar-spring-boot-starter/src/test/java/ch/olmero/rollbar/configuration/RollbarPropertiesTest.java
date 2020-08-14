package ch.olmero.rollbar.configuration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Throwables;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RollbarPropertiesTest {
	@Test
	public void missingRequiredProperties() {
		try (AnnotationConfigApplicationContext context = createContext()) {
			Assertions.fail("Expected BindException to be thrown");
		} catch (Exception e) {
			Throwable rootCause = Throwables.getRootCause(e);
			assertThat(rootCause).isInstanceOf(BindValidationException.class);

			BindValidationException bindException = (BindValidationException)rootCause;

			List<FieldError> fieldErrors = bindException.getValidationErrors().getAllErrors().stream()
					.map(o -> (FieldError) o)
					.collect(Collectors.toList());

			assertThat(fieldErrors)
					.hasSize(2)
					.extracting("field")
					.containsOnly("accessToken", "environment");
		}
	}

	@Test
	public void shouldBeValid() {
		try (AnnotationConfigApplicationContext context = createContext("com.rollbar.accessToken:abc", "com.rollbar.environment:test")) {
		}
	}

	private AnnotationConfigApplicationContext createContext(String... pairs) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

		TestPropertyValues.of(pairs).applyTo(context);

		context.register(RollbarPropertiesConfiguration.class);
		context.refresh();

		return context;
	}

	@RequiredArgsConstructor
	@Configuration
	@EnableConfigurationProperties(RollbarProperties.class)
	public static class RollbarPropertiesConfiguration {
		private final RollbarProperties rollbarProperties;
	}
}