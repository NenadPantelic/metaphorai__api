package ai.metaphor.api.service.impl;

import ai.metaphor.api.dto.response.IdentityResponse;
import ai.metaphor.api.service.IdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IdentityServiceImpl implements IdentityService {

    @Override
    public IdentityResponse getIdentity() {
        log.info("Resolving an identity from context");
        return new IdentityResponse("", "");
    }
}
