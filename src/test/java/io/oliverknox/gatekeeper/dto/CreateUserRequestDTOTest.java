package io.oliverknox.gatekeeper.dto;

import io.oliverknox.gatekeeper.annotation.ValidatorFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserRequestDTOTest {

    private static final ValidatorFixture VALIDATOR_FIXTURE = new ValidatorFixture();

    @AfterAll
    public static void tearDown() {
        VALIDATOR_FIXTURE.close();
    }

    @Test
    public void testConstructor() {
        var createUserRequestDTO = new CreateUserRequestDTO("TestUser", "Password1");

        assertEquals("TestUser", createUserRequestDTO.username());
        assertEquals("Password1", createUserRequestDTO.password());
    }

    @Test
    public void testValidation() {
        var expectedErrorMessages = new ArrayList<>(
                Arrays.asList(
                        "Username is required.",
                        "Password must contain at least one lowercase letter, one uppercase letter, and one digit.",
                        "Password must be at least 8 characters long.",
                        "Password is required.")
        );
        Collections.sort(expectedErrorMessages);
        var createUserRequestDTO = new CreateUserRequestDTO("", "");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(createUserRequestDTO);

        var actualErrorMessages = new ArrayList<String>();
        violations.iterator().forEachRemaining(item -> actualErrorMessages.add(item.getMessage()));
        Collections.sort(actualErrorMessages);

        assertEquals(expectedErrorMessages.size(), violations.size());
        assertArrayEquals(actualErrorMessages.toArray(), expectedErrorMessages.toArray());
    }
}
