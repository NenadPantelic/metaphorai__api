package ai.metaphor.api.identity;

import lombok.Builder;

@Builder
public record User(String id,
                   String displayName,
                   String username,
                   String password,
                   Role role) {
}
