package io.oliverknox.gatekeeper.dto;

import java.util.Map;

public record ErrorResponseDTO(String message, Map<String, String> errorsByField) {

    public ErrorResponseDTO(String message) {
        this(message, null);
    }
}
