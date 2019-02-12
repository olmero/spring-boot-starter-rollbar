# Rollbar Spring Boot Starter

## Enabling Rollbar
The simplest way to enable rollbar is to add a dependency to
`spring-boot-starter-rollbar`.

#### Maven
To add rollbar to a Maven-based project, add the following '`Starter`' dependency:

```xml
<dependencies>
    <dependency>
        <groupId>ch.olmero</groupId>
        <artifactId>rollbar-spring-boot-starter</artifactId>
        <version>2.0.4.RELEASE</version>
    </dependency>
</dependencies>
```

#### Gradle
To add rollbar to a Gradle-based project, add the following '`Starter`' dependency:

```groovy
apply plugin: 'maven'

compile 'ch.olmero:rollbar-spring-boot-starter:2.0.4.RELEASE'
```


## Configuration
The following properties are registered and can (must) be configured accordingly.

`com.rollbar.access-token`

The access token to use. This value **must** be configured.

`com.rollbar.environment`

Represents the current environment (e.g.: production, debug, test). This value **must** be configured.

`com.rollbar.code-version`

The currently running version of the code. This value is **optional**.

`com.rollbar.enabled`

Wheter rollbar should be enabled (default) or disabled. When disabled, a `NoopRollbarNotificationService` will be provided. This value is **optional**
