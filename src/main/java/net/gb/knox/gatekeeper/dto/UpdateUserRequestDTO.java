package net.gb.knox.gatekeeper.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(
        @NotBlank(message = "Username is required.")
        String username
) {

}
