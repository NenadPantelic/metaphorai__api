package ai.metaphor.api.auth;

import ai.metaphor.api.identity.Role;


public record Identity(String userId,
                       Role role) {
}
