package io.oliverknox.gatekeeper.annotation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Set;

public class ValidatorFixture implements AutoCloseable {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ValidatorFixture() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public Validator getValidator() {
        return validator;
    }

    public <T> ArrayList<String> getErrorMessages(Set<ConstraintViolation<T>> violations) {
        var errorMessages = new ArrayList<String>();
        violations.iterator().forEachRemaining((item) -> errorMessages.add(item.getMessage()));
        return errorMessages;
    }

    @Override
    public void close() {
        validatorFactory.close();
    }
}
