package ai.metaphor.api.service.impl;

import ai.metaphor.api.auth.AuthHandler;
import ai.metaphor.api.dto.request.AuthRequest;
import ai.metaphor.api.dto.response.AuthResponse;
import ai.metaphor.api.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthHandler authHandler;

    public AuthServiceImpl(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        log.info("Authenticating user: user={}", authRequest.username());
        String credential = authHandler.authenticate(authRequest.username(), authRequest.password());
        return new AuthResponse(credential);
    }

    @Override
    public void logout() {

        authHandler.clearAuthentication(null);
    }
}
