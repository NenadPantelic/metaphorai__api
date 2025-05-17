package ai.metaphor.api.auth.identity;

import ai.metaphor.api.identity.Role;

public record Identity(String userId,
                       String username,
                       Role role) {
}
