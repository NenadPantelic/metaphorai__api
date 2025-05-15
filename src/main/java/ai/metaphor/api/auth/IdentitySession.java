package ai.metaphor.api.auth;

import java.time.Instant;

public record IdentitySession(Identity identity,
                              Instant expirationTime) {
}
