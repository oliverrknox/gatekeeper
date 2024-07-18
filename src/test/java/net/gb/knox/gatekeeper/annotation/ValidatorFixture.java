package net.gb.knox.gatekeeper.annotation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

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

    @Override
    public void close() {
        validatorFactory.close();
    }
}
