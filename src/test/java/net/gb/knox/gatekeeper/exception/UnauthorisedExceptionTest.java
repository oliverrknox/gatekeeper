package net.gb.knox.gatekeeper.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnauthorisedExceptionTest {

    private static final String MESSAGE = "Test unauthorised exception.";
    private static final Map<String, String> ERRORS_BY_FIELD = Map.of("Error 1 key", "Error 2 value");

    @Test
    public void testConstructor() {
        var unauthorisedException = new UnauthorisedException(MESSAGE, ERRORS_BY_FIELD);

        assertEquals(MESSAGE, unauthorisedException.getMessage());
        assertEquals(ERRORS_BY_FIELD, unauthorisedException.getErrorsByField());
    }

    @Test
    public void testConstructorVariant_message() {
        var unauthorisedException = new UnauthorisedException(MESSAGE);

        assertEquals(MESSAGE, unauthorisedException.getMessage());
        assertNull(unauthorisedException.getErrorsByField());
    }
}
