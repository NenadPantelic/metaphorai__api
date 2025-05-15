package ai.metaphor.api.repository;

import ai.metaphor.api.identity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String userId);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
