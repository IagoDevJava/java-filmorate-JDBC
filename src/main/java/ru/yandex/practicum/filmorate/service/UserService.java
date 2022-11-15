package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    //    private final UserStorage userStorage;
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

//    @Autowired
//    public UserService(UserStorage userStorage) {
//        this.userStorage = userStorage;
//    }

    /**
     * получение списка пользователей
     */
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    /**
     * создание пользователя
     */
    public User createUser(User user) throws SQLException {
        return userDao.createUser(user);
    }

    /**
     * обновление пользователя
     */
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    /**
     * Удаление пользователей из списка
     */
    public void clearUsers() {
        userDao.clearUsers();
    }

    /**
     * Удаление пользователя по id
     */
    public void deleteUserById(String idStr) {
        int id = Integer.parseInt(idStr);
        userDao.deleteUserById(id);
    }

    /**
     * найти пользователя по id
     */
    public Optional<User> findUserById(String idStr) {
        return userDao.findUserById(idStr);
    }

    /**
     * добавление в друзья
     */
    public void addFriendsForUsers(String userIdStr, String friendIdStr) {
        userDao.addFriends(userIdStr, friendIdStr);
    }

    /**
     * удаление из друзей
     */
    public void deleteFriendsForUsers(String userIdStr, String friendIdStr) {
        userDao.deleteFriend(userIdStr, friendIdStr);
    }

    /**
     * возвращаем список друзей пользователя
     */
    public List<User> getFriendsUser(String idStr) {
        return userDao.getFriendsUser(idStr);
    }

    /**
     * вывод списка общих друзей
     */
    public List<User> getListCommonFriends(String id, String otherId) {
        return userDao.getListCommonFriendsDao(id, otherId);
    }
}