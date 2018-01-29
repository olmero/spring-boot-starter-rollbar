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
    </dependency>
</dependencies>
```

## Configuration
The following properties are registered and can (must) be configured accordingly.

`com.rollbar.access-token` 

The access token to use. This value **must** be configured.

`com.rollbar.environment` 

Represents the current environment (e.g.: production, debug, test). This value **must** be configured.

`com.rollbar.code-version`

The currently running version of the code. This value is **optional**.