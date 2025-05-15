package ai.metaphor.api.auth;

import ai.metaphor.api.exception.AuthException;
import ai.metaphor.api.identity.User;
import ai.metaphor.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthHandler {

    private final Map<String, IdentitySession> identityMap = new HashMap<>();
    private final UserRepository userRepository;
    private final CredentialProvider credentialProvider;

    public AuthHandler(UserRepository userRepository, CredentialProvider credentialProvider) {
        this.userRepository = userRepository;
        this.credentialProvider = credentialProvider;
    }

    public String authenticate(String username, String password) {
        log.info("Authenticating user {}...", username);
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new AuthException("Invalid credential."));
        String credential = credentialProvider.get(user);
        Identity identity = new Identity(user.id(), user.role());
        identityMap.put(
                credential,
                new IdentitySession(identity, Instant.now())
        );
        IdentityContextHolder.setAuthentication(identity);
        return credential;
    }

    public IdentitySession resolve(String credential) {
        if (credential == null || credential.isBlank()) {
            throw new AuthException("Invalid credential.");
        }

        IdentitySession identitySession = identityMap.get(credential);
        if (identitySession == null) {
            throw new AuthException("Invalid credential.");
        }

        if (!identitySession.expirationTime().isAfter(Instant.now())) {
            identityMap.remove(credential);
            throw new AuthException("Expired credentials.");
        }

        return identitySession;
    }

    public void clearAuthentication(String credential) {
        if (credential != null) {
            IdentityContextHolder.removeIdentity();
        }
    }
}
