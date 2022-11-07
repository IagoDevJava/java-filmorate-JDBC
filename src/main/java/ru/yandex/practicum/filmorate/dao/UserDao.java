package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    /**
     * получение списка пользователей
     */
    List<User> getUsers();

    /**
     * создание пользователя
     */
    User createUser(User user);

    /**
     * обновление пользователя
     */
    User updateUser(User user);

    /**
     * Удаление пользователей из списка
     */
    void clearUsers();

    /**
     * Удаление пользователя по id
     */
    void deleteUserById(int id);

    /**
     * найти пользователя по id
     */
    User findUserById(String idStr);
}
