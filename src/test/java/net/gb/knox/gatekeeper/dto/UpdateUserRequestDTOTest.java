package net.gb.knox.gatekeeper.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateUserRequestDTOTest {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    public static void tearDown() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    public void testConstructor() {
        var updateUserRequestDTO = new UpdateUserRequestDTO("UpdatedUser");

        assertEquals("UpdatedUser", updateUserRequestDTO.username());
    }

    @Test
    public void testValidation() {
        var updateUserRequestDTO = new UpdateUserRequestDTO("");

        var violations = VALIDATOR.validate(updateUserRequestDTO);

        assertEquals(1, violations.size());
        assertEquals("Username is required.", violations.iterator().next().getMessage());
    }
}
