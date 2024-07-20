package net.gb.knox.gatekeeper.exception;

import java.util.Map;

public class ResponseException extends Exception {
    private Map<String, String> errorsByField;

    public ResponseException() {
        super();
    }

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, Map<String, String> errorsByField) {
        super(message);
        this.errorsByField = errorsByField;
    }

    public Map<String, String> getErrorsByField() {
        return errorsByField;
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "message=" + getMessage() + "," +
                "errorsByField=" + errorsByField + "}";
    }
}
