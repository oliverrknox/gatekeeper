package net.gb.knox.gatekeeper.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResponseDTOTest {

    @Test
    public void testConstructor() {
        var userResponseDTO = new UserResponseDTO(1L, "TestUser");

        assertEquals(1L, userResponseDTO.id());
        assertEquals("TestUser", userResponseDTO.username());
    }
}
