package ai.metaphor.api.auth.identity;

import java.time.Instant;

public record IdentitySession(Identity identity,
                              String credential,
                              Instant expirationTime) {
}
