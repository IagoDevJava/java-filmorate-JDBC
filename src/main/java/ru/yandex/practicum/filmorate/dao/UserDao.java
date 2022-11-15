package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * получение списка пользователей
     */
    List<User> getUsers();

    /**
     * создание пользователя
     */
    User createUser(User user) throws SQLException;

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
     *
     * @return
     */
    void deleteUserById(int id);

    /**
     * найти пользователя по id
     */
    Optional<User> findUserById(String idStr);

    /**
     * добавление в друзья
     */
    void addFriends(String userIdStr, String friendIdStr);

    /**
     * удаление из друзей
     */
    void deleteFriend(String idUser, String idFriend);

    /**
     * возвращаем список друзей пользователя
     */
    List<User> getFriendsUser(String idStr);

    /**
     * вывод списка общих друзей
     */
    List<User> getListCommonFriendsDao(String id, String otherId);
}
