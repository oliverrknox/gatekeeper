package io.oliverknox.gatekeeper.dto;

import io.oliverknox.gatekeeper.annotation.ValidatorFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestDTOTest {

    private static final ValidatorFixture VALIDATOR_FIXTURE = new ValidatorFixture();

    @AfterAll
    public static void tearDown() {
        VALIDATOR_FIXTURE.close();
    }

    @Test
    public void testConstructor() {
        var loginRequestDTO = new LoginRequestDTO("TestUser", "Password1");

        assertEquals("TestUser", loginRequestDTO.username());
        assertEquals("Password1", loginRequestDTO.password());
    }

    @Test
    public void testValidation() {
        var expectedErrorMessages = new ArrayList<>(
                Arrays.asList(
                        "Username is required.",
                        "Password is required.")
        );
        Collections.sort(expectedErrorMessages);
        var loginRequestDTO = new LoginRequestDTO("", "");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(loginRequestDTO);

        var actualErrorMessages = new ArrayList<String>();
        violations.iterator().forEachRemaining(item -> actualErrorMessages.add(item.getMessage()));
        Collections.sort(actualErrorMessages);

        assertEquals(expectedErrorMessages.size(), violations.size());
        assertArrayEquals(actualErrorMessages.toArray(), expectedErrorMessages.toArray());
    }
}
