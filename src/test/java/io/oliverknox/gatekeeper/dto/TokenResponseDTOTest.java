package io.oliverknox.gatekeeper.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenResponseDTOTest {

    @Test
    public void testConstructor() {
        var tokenResponseDTO = new TokenResponseDTO("token");

        assertEquals("token", tokenResponseDTO.token());
    }
}
