package net.gb.knox.gatekeeper.dto;

import net.gb.knox.gatekeeper.annotation.ValidPassword;

public record UpdateUserRequestDTO(
        String username,
        @ValidPassword
        String password
) {

}
