package ch.olmero.rollbar.configuration;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.rollbar")
public class RollbarProperties {
	/**
	 * The access token to use.
	 */
	@NotEmpty
	private String accessToken;

	/**
	 * Represents the current environment (e.g.: production, debug, test).
	 */
	@NotEmpty
	private String environment;

	/**
	 * The currently running version of the code.
	 */
	private String codeVersion;
}
