package ch.olmero.rollbar.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@Configuration
@Import({RollbarConfiguration.class, NoOpRollbarConfiguration.class})
public class RollbarAutoConfiguration {

}
