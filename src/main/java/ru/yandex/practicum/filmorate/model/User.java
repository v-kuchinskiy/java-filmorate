package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private Set<Long> friends = new HashSet<>();

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @NoSpaces
    private String login;

    @Past
    private LocalDate birthday;

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.name = user.getName();
        this.birthday = user.getBirthday();
        this.friends = new HashSet<>(user.getFriends());
    }

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
