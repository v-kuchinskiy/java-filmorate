package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailWhenEmailInvalid() {
        User user = new User(null, "invalid-email", "login", "Name",
                LocalDate.of(1990, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("email")),
                        "Должно быть нарушение для поля email")
        );
    }

    @Test
    void shouldFailWhenLoginHasSpaces() {
        User user = new User(null, "email@mail.com", "user name", "Name",
                LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("login")),
                        "Должно быть нарушение для поля login")
        );
    }

    @Test
    void shouldFailWhenBirthdayInFuture() {
        User user = new User(null, "user@mail.com", "login", "Name",
                LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("birthday")),
                        "Должно быть нарушение для поля birthday")
        );
    }

    @Test
    void shouldPassWhenUserIsValid() {
        User user = new User(null, "user@mail.com", "login", "Name",
                LocalDate.of(1990, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // оставил одиночный assert, так как только одна проверка.
        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }
}
