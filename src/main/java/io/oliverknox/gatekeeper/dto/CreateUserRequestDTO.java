package io.oliverknox.gatekeeper.dto;

import jakarta.validation.constraints.NotBlank;
import io.oliverknox.gatekeeper.annotation.ValidPassword;

public record CreateUserRequestDTO(
        @NotBlank(message = "Username is required.")
        String username,
        @NotBlank(message = "Password is required.")
        @ValidPassword
        String password
) {

}
