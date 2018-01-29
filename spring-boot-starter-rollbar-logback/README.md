# Spring Boot Starter Rollbar

## Enabling Rollbar
The simplest way to enable rollbar through logback is to add a dependency to
`spring-boot-starter-rollbar-logback`.
 
#### Maven
To add rollbar for logback to a Maven-based project, add the following '`Starter`' dependency:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-rollbar-logback</artifactId>
    </dependency>
</dependencies>
```

## Configuration
The following properties are registered and can be configured accordingly.

`com.rollbar.logging.level.org.hibernate=WARN`

The possibilities on how to configure the logging levels are very similar to Spring Boot's `LoggingSystem` https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html

If nothing is configured, the default behaviour is to only forward `ERROR` logs to Rollbar. The general level can be
changed by setting the level for `root` e.g.

`com.rollbar.logging.level.root=WARN`

In this case everything that is logged as `WARN` or `ERROR` will be forwarded to Rollbar.