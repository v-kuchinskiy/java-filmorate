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

class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Film film = new Film(null, " ", "Description",
                LocalDate.of(2000, 1, 1), 100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("name")),
                        "Должно быть нарушение для поля name")
        );
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        String longDesc = "x".repeat(201);
        Film film = new Film(null, "Title", longDesc,
                LocalDate.of(2000, 1, 1), 100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("description")),
                        "Должно быть нарушение для поля description")
        );
    }

    @Test
    void shouldFailWhenReleaseDateTooEarly() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(1700, 1, 1), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("releaseDate")),
                        "Должно быть нарушение для поля releaseDate")
        );
    }

    @Test
    void shouldFailWhenDurationNegative() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(2000, 1, 1), -50);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertAll(
                () -> assertFalse(violations.isEmpty(), "Должны быть нарушения валидации"),
                () -> assertTrue(violations.stream().anyMatch(v ->
                                v.getPropertyPath().toString().equals("duration")),
                        "Должно быть нарушение для поля duration")
        );
    }

    @Test
    void shouldPassWhenFilmIsValid() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(2000, 1, 1), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }
}
