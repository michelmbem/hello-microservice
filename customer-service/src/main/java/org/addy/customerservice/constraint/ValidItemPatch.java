package org.addy.customerservice.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.addy.customerservice.constraint.validator.ValidItemPatchValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidItemPatchValidator.class)
public @interface ValidItemPatch {

    String message() default """
            When op is ADD payload is required.
            When op is DELETE id is required
            When op is UPDATE both id and payload are required.
            """;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
