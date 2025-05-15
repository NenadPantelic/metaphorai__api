package ai.metaphor.api.dto.response;

import java.util.List;

public record ApiError(String message,
                       int code,
                       List<String> errors) {

    public ApiError(String message, int code) {
        this(message, code, List.of());
    }
}
