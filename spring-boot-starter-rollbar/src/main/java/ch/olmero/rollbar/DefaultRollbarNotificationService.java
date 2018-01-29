package ch.olmero.rollbar;

import com.rollbar.notifier.Rollbar;
import lombok.RequiredArgsConstructor;

import static com.rollbar.api.payload.data.Level.lookupByName;

@RequiredArgsConstructor
public class DefaultRollbarNotificationService extends AbstractRollbarNotificationService {
	private final Rollbar rollbar;

	@Override
	public void log(String message, Throwable throwable, Level level) {
		this.rollbar.log(throwable,
			message,
			level != null
				? lookupByName(level.name().toLowerCase())
				: null);
	}
}
