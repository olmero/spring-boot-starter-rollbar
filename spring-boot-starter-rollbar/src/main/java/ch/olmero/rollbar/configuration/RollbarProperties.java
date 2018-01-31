package ch.olmero.rollbar.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.GitProperties;

@Data
@ConfigurationProperties(prefix = "com.rollbar")
public class RollbarProperties {
	@Setter(onMethod_ = @Autowired(required = false))
	@Getter(AccessLevel.PRIVATE)
	private GitProperties gitProperties;

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

	/**
	 * If {@code codeVersion} is set this value will be returned otherwise when {@GitProperties} are defined,
	 * the commitId of such will be returned. otherwise {@code null};
	 *
	 * @return the code version
	 */
	public String getCodeVersion() {
		if (this.codeVersion == null) {
			if (this.gitProperties != null) {
				return this.gitProperties.getCommitId();
			}
		}

		return this.codeVersion;
	}
}
