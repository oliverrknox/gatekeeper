package net.gb.knox.gatekeeper.dto;

import jakarta.validation.constraints.NotBlank;
import net.gb.knox.gatekeeper.annotation.ValidPassword;

public record CreateUserRequestDTO(
        @NotBlank(message = "Username is required.")
        String username,
        @ValidPassword
        String password
) {

}
