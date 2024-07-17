package net.gb.knox.gatekeeper.dto;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ErrorResponseDTOTest {

    @Test
    public void testConstructor() {
        var errorsByField = new HashMap<>(Map.of(
                "Error 1 key", "Error 1 value",
                "Error 2 key", "Error 2 value"));
        var errorResponseDTO = new ErrorResponseDTO("Test message.", errorsByField);

        assertEquals("Test message.", errorResponseDTO.message());
        assertEquals("Error 1 value", errorResponseDTO.errorsByField().get("Error 1 key"));
        assertEquals("Error 2 value", errorResponseDTO.errorsByField().get("Error 2 key"));
    }

    @Test
    public void testConstructorVariant_message() {
        var errorResponseDTO = new ErrorResponseDTO("Test message.");

        assertEquals("Test message.", errorResponseDTO.message());
        assertNull(errorResponseDTO.errorsByField());
    }
}
