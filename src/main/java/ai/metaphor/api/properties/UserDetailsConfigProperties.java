package ai.metaphor.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "users")
public record UserDetailsConfig(String filepath) {
}
