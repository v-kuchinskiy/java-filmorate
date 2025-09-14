package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> getUserById(Long id);

    User addUser(User user);

    void removeUser(User user);

    User updateUser(User user);

    List<User> findAll();
}
