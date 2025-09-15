package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendList(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriendList(@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.removeFriend(userId, friendId);
    }
}
