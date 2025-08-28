package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @NoSpaces
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.name = user.getName();
        this.birthday = user.getBirthday();
    }
}
