package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * получение списка пользователей
     */
    @GetMapping
    public List<User> getUsers() {
        log.debug("Список пользователей");
        return userService.getUsers();
    }

    /**
     * получение пользователя по id
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        log.debug("Получили пользователя с id: {}", id);
        return userService.findUserById(id);
    }

    /**
     * создание пользователя
     */
    @PostMapping()
    public User createUser(@RequestBody User user) {
        log.debug("Добавили: {}", user);
        return userService.createUser(user);
    }

    /**
     * обновление пользователя
     */
    @PutMapping()
    public User updateUser(@RequestBody User user) {
        log.debug("Обновили: {}", user);
        return userService.updateUser(user);
    }

    /**
     * Удаление пользователей из списка
     */
    @DeleteMapping
    public void clearUsers() {
        log.debug("Очистили список пользователей:");
        userService.clearUsers();
    }

    /**
     * Удаление пользователя по id
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id) {
        log.debug("Удалили пользователя по id: {}", id);
        userService.deleteUserById(id);
    }

    /**
     * добавление в друзья
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.debug("Добавили пользователя с id {}, в друзья к пользователю с id {}", friendId, id);
        userService.addFriendsForUsers(id, friendId);
    }

    /**
     * удаление из друзей
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        log.debug("Удалили пользователя с id {}, из друзей пользователя с id {}", friendId, id);
        userService.deleteFriendsForUsers(id, friendId);
    }

    /**
     * возвращаем список пользователей, являющихся его друзьями
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriendsUser(@PathVariable String id) {
        log.debug("Список друзей пользователя с id: {}", id);
        return userService.getFriendsUser(id);
    }

    /**
     * получение списка общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUsers(@PathVariable String id, @PathVariable String otherId) {
        log.debug("Список общих друзей пользователей: {}, {}", id, otherId);
        return userService.getListFriends(id, otherId);
    }
}