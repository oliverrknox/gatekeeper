package io.oliverknox.gatekeeper.dto;

import io.oliverknox.gatekeeper.annotation.ValidatorFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateUserRequestDTOTest {

    private static final ValidatorFixture VALIDATOR_FIXTURE = new ValidatorFixture();

    @AfterAll
    public static void tearDown() {
        VALIDATOR_FIXTURE.close();
    }

    @Test
    public void testConstructor() {
        var updateUserRequestDTO = new UpdateUserRequestDTO("UpdatedUser", "UpdatedPassword1");

        assertEquals("UpdatedUser", updateUserRequestDTO.username());
        assertEquals("UpdatedPassword1", updateUserRequestDTO.password());
    }

    @Test
    public void testValidation() {
        var expectedErrorMessages = new ArrayList<>(
                Arrays.asList(
                        "Password must contain at least one lowercase letter, one uppercase letter, and one digit.",
                        "Password must be at least 8 characters long.")
        );
        var updateUserRequestDTO = new UpdateUserRequestDTO(null, "pass");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(updateUserRequestDTO);
        var actualErrorMessages = VALIDATOR_FIXTURE.getErrorMessages(violations);

        Collections.sort(expectedErrorMessages);
        Collections.sort(actualErrorMessages);

        assertEquals(2, violations.size());
        assertArrayEquals(expectedErrorMessages.toArray(), actualErrorMessages.toArray());
    }
}
