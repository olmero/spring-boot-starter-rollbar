# Spring Boot Starter Rollbar

## Enabling Rollbar
The simplest way to enable rollbar is to add a dependency to 
`spring-boot-starter-rollbar`.
 
#### Maven
To add rollbar to a Maven-based project, add the following '`Starter`' dependency:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-rollbar</artifactId>
        <version>2.0.1.RELEASE</version>
    </dependency>
</dependencies>
```

#### Gradle
To add rollbar to a Gradle-based project, add the following '`Starter`' dependency:

```groovy
apply plugin: 'maven'

compile 'ch.olmero.:spring-boot-starter-rollbar:2.0.1.RELEASE'
```


## Configuration
The following properties are registered and can (must) be configured accordingly.

`com.rollbar.access-token` 

The access token to use. This value **must** be configured.

`com.rollbar.environment` 

Represents the current environment (e.g.: production, debug, test). This value **must** be configured.

`com.rollbar.code-version`

The currently running version of the code. This value is **optional**.

`com.rollbar.enable`

Wheter rollbar should be enabled (default) or disabled. When disabled, a `NoopRollbarNotificationService` will be provided. This value is **optional**