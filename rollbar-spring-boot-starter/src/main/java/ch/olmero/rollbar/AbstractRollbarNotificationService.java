package ch.olmero.rollbar;

public abstract class AbstractRollbarNotificationService implements RollbarNotificationService {
	@Override
	public void log(String message, Level level) {
		log(message, null, level);
	}
}
