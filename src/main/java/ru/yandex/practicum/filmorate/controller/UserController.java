package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private Integer userId = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users - получение всех пользователей");
        return users.values().stream()
                .map(User::new)
                .toList();

    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users - создание нового пользователя с login: {}", user.getLogin());

        user.setId(getNextId());

        if (!StringUtils.hasText(user.getName())) {
            log.info("Имя пользователя не указано, устанавливаем login в качестве имени: {}", user.getLogin());
            user.setName(user.getLogin());
        }

        User createdUser = new User(user);
        users.put(user.getId(), createdUser);
        log.debug("Создан пользователь с ID: {}, email: {}, login: {}",
                user.getId(), user.getEmail(), user.getLogin());
        return new User(createdUser);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("PUT /users - обновление пользователя с ID: {}", user.getId());

        if (exists(user)) {
            if (!StringUtils.hasText(user.getName())) {
                log.info("Имя пользователя не указано, устанавливаем login в качестве имени: {}", user.getLogin());
                user.setName(user.getLogin());
            }

            User updatedUser = new User(user);
            users.put(user.getId(), updatedUser);
            log.debug("Пользователь с ID: {} успешно обновлен", user.getId());
            return new User(updatedUser);
        }
        log.warn("Пользователь с ID: {} не найден для обновления", user.getId());
        throw new NotFoundException("Пользователь с ID: " + user.getId() + " не найден.");
    }

    private boolean exists(User user) {
        return user.getId() != null && users.containsKey(user.getId());
    }

    private int getNextId() {
        log.trace("Генерация следующего ID пользователя: {}", userId + 1);
        return ++userId;
    }
}
