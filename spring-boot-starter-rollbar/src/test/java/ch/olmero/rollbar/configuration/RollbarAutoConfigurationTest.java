package ch.olmero.rollbar.configuration;

import ch.olmero.rollbar.DefaultRollbarNotificationService;
import ch.olmero.rollbar.RollbarNotificationService;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.*;
import org.junit.*;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class RollbarAutoConfigurationTest {
	private AnnotationConfigApplicationContext context;

	@Before
	public void setup() {
		this.context = new AnnotationConfigApplicationContext();

		TestPropertyValues.of("com.rollbar.accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				"com.rollbar.environment=test",
				"com.rollbar.codeVersion=23218add").applyTo(this.context);

		this.context.register(RollbarAutoConfiguration.class);
		this.context.refresh();
	}

	@After
	public void after() {
		this.context.close();
	}

	@Test
	public void verifyConfiguration() {
		Rollbar rollbar = this.context.getBean(Rollbar.class);

		MockConfigProvider configProvider = new MockConfigProvider();
		rollbar.configure(configProvider);

		assertThat(configProvider.config.accessToken()).isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
		assertThat(configProvider.config.environment()).isEqualTo("test");
		assertThat(configProvider.config.codeVersion()).isEqualTo("23218add");
	}

	@Test
	public void verifyNotificationService() {
		assertThat(this.context.getBean(RollbarNotificationService.class)).isInstanceOf(DefaultRollbarNotificationService.class);
	}

	private class MockConfigProvider implements ConfigProvider {
		private Config config;

		@Override
		public Config provide(ConfigBuilder builder) {
			return this.config = builder.build();
		}
	}
}