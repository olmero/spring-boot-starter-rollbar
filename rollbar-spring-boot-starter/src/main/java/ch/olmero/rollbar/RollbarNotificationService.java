package ch.olmero.rollbar;

public interface RollbarNotificationService {
	enum Level {
		CRITICAL, ERROR, WARNING, INFO, DEBUG
	}

	void log(String message, Level level);
	void log(String message, Throwable throwable, Level level);
}
