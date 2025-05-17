package ai.metaphor.api.repository;

import ai.metaphor.api.identity.Role;
import ai.metaphor.api.identity.User;
import ai.metaphor.api.properties.UserDetailsConfigProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    private static File tempFile;

    private static final String DISPLAY_NAME = "Nenad Pantelic";
    private static final String USERNAME = "nenadp";
    private static final String PASSWORD = "pantomir123";
    private static final Role ROLE = Role.ADMIN;;

    @BeforeAll
    public static void setUp() throws IOException {
        tempFile = File.createTempFile("test-metaphorai-", ".csv");
        tempFile.deleteOnExit();

        String content = String.format("""
                        displayName,username,password,role
                        %s,%s,%s,%s
                        Panta Nenadic, pantan, 123pantomir,VIEWER
                        """,
                DISPLAY_NAME, USERNAME, PASSWORD, ROLE.name());

        try (var writer = new BufferedWriter(new FileWriter(tempFile.getPath()))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private final UserDetailsConfigProperties userDetailsConfigProperties = new UserDetailsConfigProperties(
            tempFile.getAbsolutePath()
    );

    private final UserRepository userRepository = new UserRepositoryImpl(userDetailsConfigProperties);

    @Test
    public void givenValidCredentialsWhenFindByUsernameAndPasswordShouldReturnUserOptional() {
        Optional<User> optUser = userRepository.findByUsernameAndPassword(USERNAME, PASSWORD);
        Assertions.assertThat(optUser).isNotEmpty();

        User user = optUser.get();
        Assertions.assertThat(user.displayName()).isEqualTo(DISPLAY_NAME);
        Assertions.assertThat(user.username()).isEqualTo(USERNAME);
        Assertions.assertThat(user.password()).isEqualTo(PASSWORD);
        Assertions.assertThat(user.role()).isEqualTo(ROLE);
    }

    @Test
    public void givenNonExistentCredentialsWhenFindByUsernameAndPasswordShouldReturnEmptyOptional() {
        String username = "tuname";
        String password = "tpass";

        Assertions.assertThat(userRepository.findByUsernameAndPassword(username, password)).isEmpty();
    }
}