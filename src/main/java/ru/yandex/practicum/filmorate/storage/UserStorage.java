package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    /**
     * получение списка пользователей
     */
    public List<User> getUsers();

    /**
     * создание пользователя
     */
    public User createUser(User user);

    /**
     * обновление пользователя
     */
    public User updateUser(User user);

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
    public User findUserById(String idStr);
}
