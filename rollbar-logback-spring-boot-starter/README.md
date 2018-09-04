# Rollbar Logback Spring Boot Starter

## Enabling Rollbar for Logback
The simplest way to enable rollbar through logback is to add a dependency to
`spring-boot-starter-rollbar-logback`.

#### Maven
To add rollbar for logback to a Maven-based project, add the following '`Starter`' dependency:

```xml
<dependencies>
    <dependency>
        <groupId>ch.olmero</groupId>
        <artifactId>rollbar-logback-spring-boot-starter</artifactId>
        <version>2.0.1.RELEASE</version>
    </dependency>
</dependencies>
```

#### Gradle
To add rollbar for logback to a Gradle-based project, add the following '`Starter`' dependency:

```groovy
apply plugin: 'maven'

compile 'ch.olmero:rollbar-logback-spring-boot-starter:2.0.1.RELEASE'
```


## Configuration
The following properties are registered and can be configured accordingly.

`com.rollbar.logging.level.org.hibernate=WARN`

The possibilities on how to configure the logging levels are very similar to Spring Boot's `LoggingSystem` https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html

If nothing is configured, the default behaviour is to only forward `ERROR` logs to Rollbar. The general level can be
changed by setting the level for `root` e.g.

`com.rollbar.logging.level.root=WARN`

In this case everything that is logged as `WARN` or `ERROR` will be forwarded to Rollbar.

As this module has a dependency to `rollbar-spring-boot-starter`, the rollbar configuration options can also be applied.
