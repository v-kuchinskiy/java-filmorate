package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        String longDesc = "x".repeat(201);
        Film film = new Film(null, "Title", longDesc,
                LocalDate.of(2000, 1, 1), 100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldFailWhenReleaseDateTooEarly() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(1700, 1, 1), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("releaseDate")));
    }

    @Test
    void shouldFailWhenDurationNegative() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(2000, 1, 1), -50);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        System.out.println(violations);
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void shouldPassWhenFilmIsValid() {
        Film film = new Film(null, "Title", "Desc",
                LocalDate.of(2000, 1, 1), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}
