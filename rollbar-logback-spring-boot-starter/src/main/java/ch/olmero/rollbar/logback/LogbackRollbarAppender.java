package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.RequiredArgsConstructor;

import static ch.olmero.rollbar.RollbarNotificationService.Level;
import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.classic.Level.TRACE;
import static ch.qos.logback.classic.Level.WARN;

@RequiredArgsConstructor
public class LogbackRollbarAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	public static final String NAME = "LogbackRollbarAppender";

	private final RollbarNotificationService rollbarNotificationService;

	@Override
	protected void append(ILoggingEvent iLoggingEvent) {
		Throwable throwable = unwrap((ThrowableProxy) iLoggingEvent.getThrowableProxy());

		this.rollbarNotificationService.log(iLoggingEvent.getFormattedMessage(), throwable, getLevel(iLoggingEvent));
	}

	private Level getLevel(ILoggingEvent iLoggingEvent) {
		if (iLoggingEvent.getLevel() == ERROR) {
			return Level.ERROR;
		}
		if (iLoggingEvent.getLevel() == WARN) {
			return Level.WARNING;
		}
		if (iLoggingEvent.getLevel() == INFO) {
			return Level.INFO;
		}
		if (iLoggingEvent.getLevel() == DEBUG || iLoggingEvent.getLevel() == TRACE) {
			return Level.DEBUG;
		}

		return null;
	}

	private Throwable unwrap(ThrowableProxy throwableProxy) {
		if (throwableProxy != null) {
			return throwableProxy.getThrowable();
		}

		return null;
	}
}
