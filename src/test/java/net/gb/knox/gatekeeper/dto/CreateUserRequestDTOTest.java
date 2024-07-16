package net.gb.knox.gatekeeper.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserRequestDTOTest {

    @Test
    public void testConstructor() {
        var createUserRequestDTO = new CreateUserRequestDTO("TestUser", "Password1");

        assertEquals("TestUser", createUserRequestDTO.username());
        assertEquals("Password1", createUserRequestDTO.password());
    }
}
