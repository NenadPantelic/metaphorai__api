package ai.metaphor.api.service;

import ai.metaphor.api.dto.request.AuthRequest;
import ai.metaphor.api.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    void logout();
}
