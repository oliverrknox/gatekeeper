package io.oliverknox.gatekeeper.exception;

import java.util.Map;

public class UserNotFoundException extends ResponseException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Map<String, String> errorsByField) {
        super(message, errorsByField);
    }
}
