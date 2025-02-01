package io.oliverknox.gatekeeper.dto;

import io.oliverknox.gatekeeper.annotation.ValidPassword;

public record UpdateUserRequestDTO(
        String username,
        @ValidPassword
        String password
) {

}
