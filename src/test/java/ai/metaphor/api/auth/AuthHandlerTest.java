package ai.metaphor.api.auth;

import ai.metaphor.api.auth.identity.Identity;
import ai.metaphor.api.auth.identity.IdentitySession;
import ai.metaphor.api.exception.AuthException;
import ai.metaphor.api.identity.Role;
import ai.metaphor.api.identity.User;
import ai.metaphor.api.properties.AuthConfigProperties;
import ai.metaphor.api.repository.UserRepository;
import com.github.benmanes.caffeine.cache.Cache;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CredentialProvider credentialProvider = new CredentialProvider();
    private final Cache<String, IdentitySession> identitySessionCache = Mockito.mock(Cache.class);
    private final AuthHandler authHandler = new AuthHandler(
            userRepository, credentialProvider, identitySessionCache,
            new AuthConfigProperties(100, 100)
    );

    @Test
    public void givenUserWhenAuthenticatedResolveTokenShouldReturnUser() {
        String id = "tid";
        String username = "tusername";
        String password = "tpassword";
        Role role = Role.VIEWER;

        User user = User.builder()
                .id(id)
                .displayName("tdisplayName")
                .username(username)
                .password(password)
                .role(Role.VIEWER)
                .build();

        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsernameAndPassword(username, password);
        String credential = authHandler.authenticate(username, password);
        Assertions.assertThat(credential).isNotBlank();

        IdentitySession expectedSession = new IdentitySession(
                new Identity(id, username, role),
                credential, Instant.now().plus(Duration.of(1000, ChronoUnit.MINUTES))
        );
        Mockito.doReturn(expectedSession).when(identitySessionCache).getIfPresent(credential);

        IdentitySession identitySession = authHandler.resolve(credential);
        Assertions.assertThat(identitySession.credential()).isEqualTo(credential);

        Identity identity = identitySession.identity();
        Assertions.assertThat(identity.userId()).isEqualTo(id);
        Assertions.assertThat(identity.username()).isEqualTo(username);
        Assertions.assertThat(identity.role()).isEqualTo(role);
    }

    @Test
    public void givenNonExistentUserCredentialsWhenAuthenticateShouldThrowException() {
        String username = "tusername";
        String password = "tpassword";

        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findByUsernameAndPassword(username, password);

        Assertions.assertThatThrownBy(() -> authHandler.authenticate(username, password))
                .isInstanceOf(AuthException.class);
    }

    @Test
    public void givenNullCredentialWhenResolveShouldThrowException() {
        Assertions.assertThatThrownBy(() -> authHandler.resolve(null))
                .isInstanceOf(AuthException.class);
    }

    @Test
    public void givenEmptyCredentialWhenResolveShouldThrowException() {
        Assertions.assertThatThrownBy(() -> authHandler.resolve(""))
                .isInstanceOf(AuthException.class);
    }

    @Test
    public void givenFakeCredentialWhenResolveShouldThrowException() {
        String credential = "maliciouscred";
        Mockito.doReturn(null).when(identitySessionCache).getIfPresent(credential);
        Assertions.assertThatThrownBy(() -> authHandler.resolve(credential))
                .isInstanceOf(AuthException.class);
    }

    @Test
    public void givenExpiredCredentialWhenResolveShouldThrowException() {
        String credential = "expiredcred";
        IdentitySession identitySession = new IdentitySession(
                new Identity("tuid", "tuname", Role.ADMIN),
                credential, Instant.now().minus(Duration.of(1000, ChronoUnit.MINUTES))
        );
        Mockito.doReturn(identitySession).when(identitySessionCache).getIfPresent(credential);
        Assertions.assertThatThrownBy(() -> authHandler.resolve(credential))
                .isInstanceOf(AuthException.class);
    }

    @Test
    public void givenNullCredentialWhenClearCacheShouldDoNothing() {
        authHandler.clearAuthentication(null);
        Mockito.verify(identitySessionCache, Mockito.never()).invalidate(Mockito.anyString());
    }

    @Test
    public void givenNonNullCredentialWhenClearCacheShouldInvalidateIt() {
        String credential = "tcred";
        authHandler.clearAuthentication(credential);
        Mockito.verify(identitySessionCache, Mockito.times(1)).invalidate(credential);
    }
}