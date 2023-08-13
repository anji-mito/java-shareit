package ru.practicum.shareit.validation.annotation;

import ru.practicum.shareit.validation.validator.EndBeforeStartValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndBeforeStartValidator.class)
@Documented
public @interface EndBeforeStart {
    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}