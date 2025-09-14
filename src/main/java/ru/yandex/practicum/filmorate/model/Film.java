package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;
    private Set<Long> likes = new HashSet<>();

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @MinDate("1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private long duration;

    public Film(Film film) {
        this.id = film.getId();
        this.name = film.getName();
        this.description = film.getDescription();
        this.releaseDate = film.getReleaseDate();
        this.duration = film.getDuration();
        this.likes = new HashSet<>(film.getLikes());
    }

    public void likeFilm(Long filmId) {
        likes.add(filmId);
    }

    public void unlikeFilm(Long filmId) {
        likes.remove(filmId);
    }
}
