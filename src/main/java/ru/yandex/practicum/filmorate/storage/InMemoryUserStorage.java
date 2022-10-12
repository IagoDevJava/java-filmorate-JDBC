package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idUser = 0;

    /**
     * получение списка пользователей
     */
    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * создание пользователя
     */
    @Override
    public User createUser(User user) {
        UserValidator.isValidNameUsers(user);
        generateIdUsers();
        user.setId(idUser);
        users.put(user.getId(), user);
        return user;
    }

    /**
     * обновление пользователя
     */
    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            UserValidator.isValidNameUsers(user);
            users.put(user.getId(), user);
        } else {
            throw new NotFoundUserException("Такого пользователя нет в базе.");
        }
        return user;
    }

    /**
     * Удаление пользователей из списка
     */
    @Override
    public void clearUsers() {
        if (!users.isEmpty()) {
            users.clear();
        }
    }

    /**
     * Удаление пользователя по id
     */
    public void deleteUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundUserException(String.format("Пользователь № %d не найден", id));
        }
        users.remove(id);
    }

    /**
     * найти пользователя по id
     */
    public User findUserById(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!users.containsKey(id)) {
            throw new NotFoundUserException(String.format("Пользователь № %d не найден", id));

        }
        return getUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst().orElseThrow(
                        () -> new NotFoundUserException(String.format("Пользователь № %d не найден", id)));
    }

    /**
     * создание уникального id пользователя
     */
    private int generateIdUsers() {
        return ++idUser;
    }
}
