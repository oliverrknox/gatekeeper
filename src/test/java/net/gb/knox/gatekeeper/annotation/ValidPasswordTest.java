package net.gb.knox.gatekeeper.annotation;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidPasswordTest {

    private static final ValidatorFixture VALIDATOR_FIXTURE = new ValidatorFixture();
    private static final String SIZE_CONSTRAINT_MESSAGE = "Password must be at least 8 characters long.";
    private static final String REGEX_CONSTRAINT_MESSAGE = "Password must contain at least one lowercase letter, one uppercase letter, and one digit.";

    @AfterAll()
    public static void tearDown() {
        VALIDATOR_FIXTURE.close();
    }

    @Test
    public void testValid() {
        var testModel = new TestModel("Password1");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(testModel);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testSizeConstraint() {
        var testModel = new TestModel("1234567");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(testModel);
        var errorMessages = getErrorMessages(violations);

        assertTrue(
                errorMessages.contains(SIZE_CONSTRAINT_MESSAGE),
                "Expected " + SIZE_CONSTRAINT_MESSAGE + " to be in list " + errorMessages
        );
    }

    @Test
    public void testRegexConstraint() {
        var testModel = new TestModel("password");

        var violations = VALIDATOR_FIXTURE.getValidator().validate(testModel);
        var errorMessages = getErrorMessages(violations);

        assertTrue(
                errorMessages.contains(REGEX_CONSTRAINT_MESSAGE),
                "Expected " + REGEX_CONSTRAINT_MESSAGE + " to be in list " + errorMessages
        );
    }

    private ArrayList<String> getErrorMessages(Set<ConstraintViolation<TestModel>> violations) {
        var errorMessages = new ArrayList<String>();
        violations.iterator().forEachRemaining((item) -> errorMessages.add(item.getMessage()));
        return errorMessages;
    }

    private record TestModel(@ValidPassword String field) {
    }
}
