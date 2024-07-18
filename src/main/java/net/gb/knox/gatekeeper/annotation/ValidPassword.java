package net.gb.knox.gatekeeper.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "Password is required.")
@Size(min = 8, message = "Password must be at least 8 characters long.")
@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).*$",
        message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit.")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
