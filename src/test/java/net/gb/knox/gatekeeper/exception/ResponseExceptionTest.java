package net.gb.knox.gatekeeper.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResponseExceptionTest {

    private static final String MESSAGE = "Test response exception.";
    private static final Map<String, String> ERRORS_BY_FIELD = Map.of("Error 1 key", "Error 2 value");

    @Test
    public void testConstructor() {
        var responseException = new ResponseException(MESSAGE, ERRORS_BY_FIELD);

        assertEquals(MESSAGE, responseException.getMessage());
        assertEquals(ERRORS_BY_FIELD, responseException.getErrorsByField());
    }

    @Test
    public void testConstructorVariant_message() {
        var responseException = new ResponseException(MESSAGE);

        assertEquals(MESSAGE, responseException.getMessage());
        assertNull(responseException.getErrorsByField());
    }
}
