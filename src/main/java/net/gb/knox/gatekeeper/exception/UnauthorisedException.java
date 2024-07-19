package net.gb.knox.gatekeeper.exception;

import java.util.Map;

public class UnauthorisedException extends ResponseException {
    public UnauthorisedException() {
        super();
    }

    public UnauthorisedException(String message) {
        super(message);
    }

    public UnauthorisedException(String message, Map<String, String> errorsByField) {
        super(message, errorsByField);
    }
}
