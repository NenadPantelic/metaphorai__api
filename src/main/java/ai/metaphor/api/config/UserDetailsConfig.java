package ai.metaphor.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "users")
public record UserDetailsConfig(String filepath) {
}
