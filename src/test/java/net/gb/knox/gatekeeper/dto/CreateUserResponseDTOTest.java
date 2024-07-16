package net.gb.knox.gatekeeper.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserResponseDTOTest {

    @Test
    public void testConstructor() {
        var createUserResponseDTO = new CreateUserResponseDTO(1L, "TestUser");

        assertEquals(1L, createUserResponseDTO.id());
        assertEquals("TestUser", createUserResponseDTO.username());
    }
}
