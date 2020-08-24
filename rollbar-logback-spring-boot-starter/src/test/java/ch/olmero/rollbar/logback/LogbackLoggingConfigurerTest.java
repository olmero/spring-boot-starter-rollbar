package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class LogbackLoggingConfigurerTest {

	private LogbackLoggingConfigurer logbackLoggingConfigurer;
	@Mock
	private RollbarNotificationService rollbarNotificationService;
	private MockEnvironment environment;

	private Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	@BeforeEach
	public void setup() {
		this.environment = new MockEnvironment();
		logbackLoggingConfigurer = new LogbackLoggingConfigurer(this.environment);
	}

	@AfterEach
	public void rest() {
		((Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).detachAppender(LogbackRollbarAppender.NAME);
	}

	@Test
	public void rootLevelDefault() {
		logbackLoggingConfigurer.configure(rollbarNotificationService);
		rollbarAppender().doAppend(loggingEvent(Level.ERROR, "message"));
		verify(this.rollbarNotificationService).log("message", null, RollbarNotificationService.Level.ERROR);

		rollbarAppender().doAppend(loggingEvent(Level.WARN, "message"));
		verify(this.rollbarNotificationService, never()).log("message", null, RollbarNotificationService.Level.WARNING);
	}

	@Test
	public void overrideRootLevel() {
		this.environment.setProperty("com.rollbar.logging.level.root", "INFO");
		logbackLoggingConfigurer.configure(rollbarNotificationService);

		rollbarAppender().doAppend(loggingEvent(Level.INFO, "message"));
		verify(this.rollbarNotificationService).log("message", null, RollbarNotificationService.Level.INFO);

		rollbarAppender().doAppend(loggingEvent(Level.WARN, "message"));
		verify(this.rollbarNotificationService).log("message", null, RollbarNotificationService.Level.WARNING);

		rollbarAppender().doAppend(loggingEvent(Level.DEBUG, "message"));
		verify(this.rollbarNotificationService, never()).log("message", null, RollbarNotificationService.Level.DEBUG);
	}

	@Test
	public void additionalLevelDefinitionIsEvaluatedHigherThanRoot() {
		this.environment
			.withProperty("com.rollbar.logging.level.root", "WARN")
			.withProperty("com.rollbar.logging.level.ch.olmero.rollbar.logback", "DEBUG");
		logbackLoggingConfigurer.configure(rollbarNotificationService);


		rollbarAppender().doAppend(loggingEvent(Level.INFO, "message"));
		verify(this.rollbarNotificationService).log("message", null, RollbarNotificationService.Level.INFO);
	}

	@Test
	public void additionalLevelDefinitionIsOFF() {
		this.environment
			.withProperty("com.rollbar.logging.level.root", "WARN")
			.withProperty("com.rollbar.logging.level.ch.olmero.rollbar.logback", "OFF");
		logbackLoggingConfigurer.configure(rollbarNotificationService);


		rollbarAppender().doAppend(loggingEvent(Level.INFO, "message"));
		verify(this.rollbarNotificationService, never()).log("message", null, RollbarNotificationService.Level.INFO);
	}

	private LogbackRollbarAppender rollbarAppender() {
		return (LogbackRollbarAppender)this.rootLogger.getAppender(LogbackRollbarAppender.NAME);
	}

	private ILoggingEvent loggingEvent(Level level, String message) {
		Logger logger = (Logger)LoggerFactory.getLogger(getClass());
		return new LoggingEvent("", logger, level, message, null, null);
	}

}