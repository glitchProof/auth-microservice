package org.glitchproof.auth.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Schema(description = "Standard api error response layout")
public record ApiExceptionResponse(

        @Schema(description = "HTTP Status Name", example = "400")
        Integer status,

        @Schema(description = "Internal Application Error Code", example = "10103")
        Integer code,

        @Schema(description = "Detailed error message", example = "Jwt is expired")
        String detail,

        @Schema(description = "Map of field validation failures (if applicable)", example = "{\"email\": \"Email is malformed\"}")
        Map<String, String> violations,

        @Schema(description = "Timestamp when the error occurred")
        LocalDateTime timestamp
) {
    public ApiExceptionResponse(HttpStatus status, Integer code, String detail, Map<String, String> violations) {
        this(status.value(), code, detail, violations, LocalDateTime.now());
    }
}
