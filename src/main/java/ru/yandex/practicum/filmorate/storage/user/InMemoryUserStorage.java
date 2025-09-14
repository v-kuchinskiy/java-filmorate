package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("dev")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger userId = new AtomicInteger(0);

    @Override
    public Optional<User> getUserById(Long id) {
        log.debug("Получение пользователя по ID: {}", id);

        if (id == null || !users.containsKey(id)) {
            log.warn("Пользователь с ID {} не найден", id);
            return Optional.empty();
        }
        return Optional.of(new User(users.get(id)));
    }

    @Override
    public List<User> findAll() {
        log.info("Получение всех пользователей");
        return users.values().stream()
                .map(User::new)
                .toList();
    }

    @Override
    public List<User> getUsersByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return ids.stream()
                .filter(id -> id != null && users.containsKey(id))
                .map(users::get)
                .map(User::new)
                .collect(Collectors.toList());
    }

    @Override
    public User addUser(User user) {
        log.info("Создание нового пользователя с login: {}", user.getLogin());
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не указано, устанавливаем login в качестве имени: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        User createdUser = new User(user);
        users.put(user.getId(), createdUser);
        log.debug("Создан пользователь с ID: {}, email: {}, login: {}",
                user.getId(), user.getEmail(), user.getLogin());
        return new User(createdUser);
    }

    @Override
    public void removeUser(User user) {
        log.debug("Удаление пользователя: {}", user);
        if (!exists(user)) {
            log.error("Попытка удалить несуществующего пользователя: {}", user);
            throw new NotFoundException("Пользователь для удаления не найден.");
        }
        users.remove(user.getId());
        log.info("Пользователь с ID {} успешно удален", user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя с ID: {}", user.getId());
        if (exists(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
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

    private Long getNextId() {
        return (long) userId.incrementAndGet();
    }
}
