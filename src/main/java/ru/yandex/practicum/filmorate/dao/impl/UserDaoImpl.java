package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    /**
     * получение списка пользователей
     */
    @Override
    public List<User> getUsers() {
        return null;
    }

    /**
     * создание пользователя
     */
    @Override
    public User createUser(User user) {
        return null;
    }

    /**
     * обновление пользователя
     */
    @Override
    public User updateUser(User user) {
        return null;
    }

    /**
     * Удаление пользователей из списка
     */
    @Override
    public void clearUsers() {

    }

    /**
     * Удаление пользователя по id
     */
    @Override
    public void deleteUserById(int id) {

    }

    /**
     * найти пользователя по id
     */
    @Override
    public User findUserById(String idStr) {
        return null;
    }
}
