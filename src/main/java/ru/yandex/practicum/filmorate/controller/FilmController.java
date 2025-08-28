package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmId = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET /films - получение всех фильмов");
        return films.values().stream()
                .map(Film::new)
                .toList();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST /films - создание нового фильма: {}", film.getName());
        film.setId(getNextId());
        Film createdFilm = new Film(film);
        films.put(film.getId(), createdFilm);
        log.debug("Создан фильм с ID: {}", film.getId());
        return new Film(createdFilm);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT /films - обновление фильма с ID: {}", film.getId());
        if (films.containsKey(film.getId())) {
            Film updatedFilm = new Film(film);
            films.put(film.getId(), updatedFilm);
            log.debug("Фильм с ID: {} успешно обновлен", film.getId());
            return new Film(updatedFilm);
        }
        log.warn("Фильм с ID: {} не найден для обновления", film.getId());
        throw new NotFoundException("Фильм с ID: " + film.getId() + " не найден.");
    }

    private int getNextId() {
        log.trace("Генерация следующего ID: {}", filmId + 1);
        return ++filmId;
    }
}
