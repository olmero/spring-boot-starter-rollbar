package ch.olmero.rollbar.logback;

import ch.olmero.rollbar.RollbarNotificationService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LogbackLoggingApplicationListener implements GenericApplicationListener {
	private static Class<?>[] SOURCE_TYPES = { SpringApplication.class, ApplicationContext.class };

	private static final LogLevels<Level> LEVELS = new LogLevels<>();

	static {
		LEVELS.map(LogLevel.TRACE, Level.ALL);
		LEVELS.map(LogLevel.TRACE, Level.TRACE);
		LEVELS.map(LogLevel.DEBUG, Level.DEBUG);
		LEVELS.map(LogLevel.INFO, Level.INFO);
		LEVELS.map(LogLevel.WARN, Level.WARN);
		LEVELS.map(LogLevel.ERROR, Level.ERROR);
		LEVELS.map(LogLevel.FATAL, Level.ERROR);
		LEVELS.map(LogLevel.OFF, Level.OFF);
	}

	@Override
	public boolean supportsEventType(ResolvableType resolvableType) {
		return ContextRefreshedEvent.class.isAssignableFrom(resolvableType.getRawClass());
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return sourceType != null
			&& Arrays.stream(SOURCE_TYPES).anyMatch(a -> a.isAssignableFrom(sourceType));
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			onApplicationPreparedEvent((ContextRefreshedEvent)event);
		}
	}

	@Override
	public int getOrder() {
		// Should run right after LoggingApplicationListener
		return LoggingApplicationListener.DEFAULT_ORDER + 1;
	}

	private void onApplicationPreparedEvent(ContextRefreshedEvent event) {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

		// default is com.rollbar.logging.level.root=ERROR
		Binder binder = Binder.get(event.getApplicationContext().getEnvironment());

		Map<String, String> levels = binder.bind("com.rollbar.logging.level",
				Bindable.mapOf(String.class, String.class)).orElseGet(Collections::emptyMap);

		Map<String, Level> logLevels = levels.entrySet().stream()
			.map(e -> new HashMap.SimpleEntry<>(e.getKey(), coerceLogLevel(e.getValue())))
			.collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

		ClassExclusionEvaluator classExclusionEvaluator = new ClassExclusionEvaluator(logLevels);
		classExclusionEvaluator.start();

		EvaluatorFilter<ILoggingEvent> eventEvaluatorFilter = new EvaluatorFilter<>();
		eventEvaluatorFilter.setOnMatch(FilterReply.ACCEPT);
		eventEvaluatorFilter.setOnMismatch(FilterReply.DENY);
		eventEvaluatorFilter.setEvaluator(classExclusionEvaluator);
		eventEvaluatorFilter.start();

		RollbarNotificationService rollbarNotificationService = event.getApplicationContext().getBean(RollbarNotificationService.class);

		LogbackRollbarAppender logbackRollbarAppender = new LogbackRollbarAppender(rollbarNotificationService);
		logbackRollbarAppender.addFilter(eventEvaluatorFilter);
		logbackRollbarAppender.setName(LogbackRollbarAppender.NAME);
		logbackRollbarAppender.setContext(rootLogger.getLoggerContext());
		logbackRollbarAppender.start();

		rootLogger.addAppender(logbackRollbarAppender);
	}

	private Level coerceLogLevel(String level) {
		if ("false".equalsIgnoreCase(level)) {
			return Level.OFF;
		}

		LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
		return LEVELS.convertSystemToNative(logLevel);
	}

	@RequiredArgsConstructor
	private static class ClassExclusionEvaluator extends EventEvaluatorBase<ILoggingEvent> {
		// We have to exclude rollbar itself from this appender, as otherwise endless cycles might occur
		private final static String[] EXCLUSIONS = { "com.rollbar" };
		private final static Level DEFAULT = Level.ERROR;

		private final Map<String, Level> levels;

		@Override
		public boolean evaluate(ILoggingEvent iLoggingEvent) throws NullPointerException {
			if (!preEvaluate(iLoggingEvent)) {
				return false;
			}

			Level currentLogLevel = iLoggingEvent.getLevel();
			Level supportedLogLevel = findLogLevel(iLoggingEvent.getLoggerName());

			if (supportedLogLevel == null) {
				for (Map.Entry<String, Level> entry : levels.entrySet()) {
					String name = entry.getKey();
					if (name.equalsIgnoreCase(LoggingSystem.ROOT_LOGGER_NAME)) {
						// take the definition for root
						supportedLogLevel = entry.getValue();
					}
				}

				if (supportedLogLevel == null) {
					supportedLogLevel = DEFAULT;
				}
			}

			return currentLogLevel.isGreaterOrEqual(supportedLogLevel);
		}

		private boolean preEvaluate(ILoggingEvent iLoggingEvent) {
			return Arrays.stream(EXCLUSIONS)
				.noneMatch(name -> iLoggingEvent.getLoggerName().startsWith(name));
		}

		private Level findLogLevel(String loggerName) {
			Level logLevel = levels.get(loggerName);

			if (logLevel == null) {
				int lastIndexOf = loggerName.lastIndexOf('.');

				if (lastIndexOf > -1) {
					return findLogLevel(loggerName.substring(0, lastIndexOf));
				}
			}

			return logLevel;
		}
	}

	private static class LogLevels<T> {
		private final Map<LogLevel, T> systemToNative = new HashMap<>();
		private final Map<T, LogLevel> nativeToSystem = new HashMap<>();

		void map(LogLevel system, T nativeLevel) {
			if (!this.systemToNative.containsKey(system)) {
				this.systemToNative.put(system, nativeLevel);
			}
			if (!this.nativeToSystem.containsKey(nativeLevel)) {
				this.nativeToSystem.put(nativeLevel, system);
			}
		}

		LogLevel convertNativeToSystem(T level) {
			return this.nativeToSystem.get(level);
		}
		T convertSystemToNative(LogLevel level) {
			return this.systemToNative.get(level);
		}
	}
}
