package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private final AtomicInteger filmId = new AtomicInteger(0);

    @Override
    public Optional<Film> getFilmById(Long id) {
        log.debug("Получение фильма по ID: {}", id);

        if (id == null || !films.containsKey(id)) {
            log.warn("Фильм с ID {} не найден", id);
            return Optional.empty();
        }
        return Optional.of(new Film(films.get(id)));
    }

    @Override
    public List<Film> findPopularFilms(int limit) {
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(limit)
                .map(Film::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findAll() {
        log.debug("Получение всех фильмов");
        return films.values().stream()
                .map(Film::new)
                .toList();
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Создание нового фильма: {}", film.getName());
        film.setId(getNextId());
        Film createdFilm = new Film(film);
        films.put(film.getId(), createdFilm);
        log.debug("Создан фильм с ID: {}", film.getId());
        return new Film(createdFilm);
    }

    @Override
    public void removeFilm(Film film) {
        log.debug("Удаление фильма: {}", film);
        if (!exists(film)) {
            log.error("Попытка удалить несуществующий фильм: {}", film);
            throw new NotFoundException("Фильм для удаления не найден.");
        }
        films.remove(film.getId());
        log.info("Фильм с ID {} успешно удален", film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID: {}", film.getId());

        if (exists(film)) {
            Film updatedFilm = new Film(film);
            films.put(film.getId(), updatedFilm);

            log.debug("Фильм с ID: {} успешно обновлен", film.getId());
            return new Film(updatedFilm);
        }
        log.warn("Фильм с ID: {} не найден для обновления", film.getId());
        throw new NotFoundException("Фильм с ID: " + film.getId() + " не найден.");
    }

    private boolean exists(Film film) {
        return film.getId() != null && films.containsKey(film.getId());
    }

    private Long getNextId() {
        return (long) filmId.incrementAndGet();
    }
}
