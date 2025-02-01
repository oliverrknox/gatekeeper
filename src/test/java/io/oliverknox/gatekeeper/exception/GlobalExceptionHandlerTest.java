package io.oliverknox.gatekeeper.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    private static final String MESSAGE = "Test exception message.";
    private static final Map<String, String> ERRORS_BY_FIELD = Map.of("Error key", "Error value");
    private static final WebRequest WEB_REQUEST_MOCK = mock(WebRequest.class);

    @BeforeEach()
    public void Setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleUserNotFoundException() {
        final var userNotFoundException = new UserNotFoundException(MESSAGE, ERRORS_BY_FIELD);

        var responseEntity = globalExceptionHandler.handleUserNotFoundException(userNotFoundException, WEB_REQUEST_MOCK);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MESSAGE, responseEntity.getBody().message());
        assertEquals(ERRORS_BY_FIELD, responseEntity.getBody().errorsByField());
    }

    @Test
    public void testHandleValidationException() {
        final var bindingResult = new BeanPropertyBindingResult(null, "ObjectName");
        bindingResult.addError(new FieldError("ObjectName", "Field", "Validation message."));
        final var methodParameter = new MethodParameter(this.getClass().getMethods()[0], -1);
        final var methodArgumentNotValidException = new MethodArgumentNotValidException(methodParameter, bindingResult);

        var responseEntity = globalExceptionHandler.handleValidationException(methodArgumentNotValidException, WEB_REQUEST_MOCK);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation failed.", responseEntity.getBody().message());
        assertNotNull(responseEntity.getBody().errorsByField());
        assertEquals("Validation message.", responseEntity.getBody().errorsByField().get("Field"));
    }

    @Test
    public void testHandleUnauthorisedException() {
        final var unauthorisedException = new UnauthorisedException(MESSAGE, ERRORS_BY_FIELD);

        var responseEntity = globalExceptionHandler.handleUnauthorisedException(unauthorisedException, WEB_REQUEST_MOCK);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MESSAGE, responseEntity.getBody().message());
        assertEquals(ERRORS_BY_FIELD, responseEntity.getBody().errorsByField());
    }

    @Test
    public void testHandleGenericException() {
        var responseEntity = globalExceptionHandler.handleGenericException(new Exception(), WEB_REQUEST_MOCK);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Something went wrong.", responseEntity.getBody().message());
        assertNull(responseEntity.getBody().errorsByField());
    }
}
