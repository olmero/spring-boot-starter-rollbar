package ch.olmero.rollbar;

public class NoOpRollbarNotificationService extends AbstractRollbarNotificationService {
	@Override
	public void log(String message, Throwable throwable, Level level) {
	}
}
