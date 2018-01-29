package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LogbackRollbarAppenderTest {
	@Mock
	private RollbarNotificationService rollbarNotificationService;
	@InjectMocks
	private LogbackRollbarAppender rollbarAppender;

	@Test
	public void levelMapping() {
		this.rollbarAppender.append(loggingEvent(Level.ALL, "all"));
		verify(this.rollbarNotificationService).log("all", null, null);

		this.rollbarAppender.append(loggingEvent(Level.TRACE, "trace"));
		verify(this.rollbarNotificationService).log("trace", null, RollbarNotificationService.Level.DEBUG);

		this.rollbarAppender.append(loggingEvent(Level.DEBUG, "debug"));
		verify(this.rollbarNotificationService).log("debug", null, RollbarNotificationService.Level.DEBUG);

		this.rollbarAppender.append(loggingEvent(Level.INFO, "info"));
		verify(this.rollbarNotificationService).log("info", null, RollbarNotificationService.Level.INFO);

		this.rollbarAppender.append(loggingEvent(Level.WARN, "warn"));
		verify(this.rollbarNotificationService).log("warn", null, RollbarNotificationService.Level.WARNING);

		this.rollbarAppender.append(loggingEvent(Level.ERROR, "error"));
		verify(this.rollbarNotificationService).log("error", null, RollbarNotificationService.Level.ERROR);

		verifyZeroInteractions(this.rollbarNotificationService);
	}

	@Test
	public void appendWithThrowable() {
		Exception exception = new Exception("exception");
		this.rollbarAppender.append(loggingEvent(Level.ALL, "all", exception));
		verify(this.rollbarNotificationService).log("all", exception, null);
	}

	private ILoggingEvent loggingEvent(Level level, String message) {
		return loggingEvent(level, message, null);
	}

	private ILoggingEvent loggingEvent(Level level, String message, Throwable throwable) {
		Logger logger = (Logger) LoggerFactory.getLogger(getClass());
		return new LoggingEvent("", logger, level, message, throwable, null);
	}
}