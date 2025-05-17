package ai.metaphor.api.auth;

import ai.metaphor.api.identity.Role;
import ai.metaphor.api.identity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class CredentialProviderTest {

    @Test
    public void testGetToken() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .displayName("test user")
                .role(Role.ADMIN)
                .username("tu012345")
                .password("tpass123")
                .build();

        CredentialProvider credentialProvider = new CredentialProvider();
        String credential = credentialProvider.get(user);
        Assertions.assertThat(credential).isNotBlank();
    }
}