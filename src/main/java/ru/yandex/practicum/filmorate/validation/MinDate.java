package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinDate {
    String value();

    String message() default "Дата не должна быть раньше {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
