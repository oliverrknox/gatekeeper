package net.gb.knox.gatekeeper.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserNotFoundExceptionTest {

    private static final String MESSAGE = "Test response exception.";
    private static final Map<String, String> ERRORS_BY_FIELD = Map.of("Error 1 key", "Error 2 value");

    @Test
    public void testConstructor() {
        var userNotFoundException = new UserNotFoundException(MESSAGE, ERRORS_BY_FIELD);

        assertEquals(MESSAGE, userNotFoundException.getMessage());
        assertEquals(ERRORS_BY_FIELD, userNotFoundException.getErrorsByField());
    }

    @Test
    public void testConstructorVariant_message() {
        var userNotFoundException = new UserNotFoundException(MESSAGE);

        assertEquals(MESSAGE, userNotFoundException.getMessage());
        assertNull(userNotFoundException.getErrorsByField());
    }
}
