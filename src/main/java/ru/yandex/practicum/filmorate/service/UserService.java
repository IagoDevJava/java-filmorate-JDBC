package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * получение списка пользователей
     */
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    /**
     * создание пользователя
     */
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    /**
     * обновление пользователя
     */
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    /**
     * Удаление пользователей из списка
     */
    public void clearUsers() {
        userStorage.clearUsers();
    }

    /**
     * Удаление пользователя по id
     */
    public void deleteUserById(String idStr) {
        int id = Integer.parseInt(idStr);
        userStorage.deleteUserById(id);
    }

    /**
     * найти пользователя по id
     */
    public User findUserById(String idStr) {
        return userStorage.findUserById(idStr);
    }

    /**
     * добавление в друга к пользователю
     */
    public void addFriendsForUsers(String userIdStr, String friendIdStr) {
        addFriends(userIdStr, friendIdStr, userStorage.findUserById(userIdStr));
        addFriends(friendIdStr, userIdStr, userStorage.findUserById(friendIdStr));
    }

    /**
     * добавление в друзья
     */
    private void addFriends(String idUser, String idFriend, User userById) {
        int id = Integer.parseInt(idUser);
        int friendId = Integer.parseInt(idFriend);
        UserValidator.isValidIdUsers(id);
        UserValidator.isValidIdUsers(friendId);
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(idUser));
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(idFriend));

        if (isFriendsByUser(idUser)) {
            TreeSet<Integer> friends = userById.getFriends();
            friends.add(friendId);
            userById.setFriends(friends);
        } else {
            TreeSet<Integer> friends = new TreeSet<>();
            friends.add(friendId);
            userById.setFriends(friends);
        }
    }

    /**
     * проверка наличия списка друзей
     */
    private Boolean isFriendsByUser(String id) {
        return userStorage.findUserById(id).getFriends() != null;
    }

    /**
     * удаление друга у пользователю
     */
    public void deleteFriendsForUsers(String userIdStr, String friendIdStr) {
        deleteFriend(userIdStr, friendIdStr, userStorage.findUserById(userIdStr));
        deleteFriend(friendIdStr, userIdStr, userStorage.findUserById(friendIdStr));
    }

    /**
     * удаление из друзей
     */
    public void deleteFriend(String idUser, String idFriend, User userById) {
        int id = Integer.parseInt(idUser);
        int friendId = Integer.parseInt(idFriend);
        UserValidator.isValidIdUsers(id);
        UserValidator.isValidIdUsers(friendId);
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(idUser));
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(idFriend));

        if (isFriendsByUser(idUser)) {
            if (!userById.getFriends().contains(friendId)) {
                throw new NotFoundUserException(String.format(
                        "У пользователя № %d нет в друзьях пользователя № %d", id, friendId));
            }
            TreeSet<Integer> friends1 = userById.getFriends();
            friends1.remove(friendId);
            userById.setFriends(friends1);
        } else {
            throw new NotFoundUserException(String.format("У пользователя № %d нет друзей", id));
        }
    }

    /**
     * возвращаем список пользователей, являющихся его друзьями
     */
    public List<User> getFriendsUser(String idStr) {
        List<Integer> idFriendsList = new ArrayList<>(userStorage.findUserById(idStr).getFriends());
        List<User> friendsList = new ArrayList<>();
        for (Integer idFriends : idFriendsList) {
            friendsList.add(userStorage.findUserById(String.valueOf(idFriends)));
        }
        return friendsList;
    }

    /**
     * вывод списка общих друзей
     */
    public List<User> getListFriends(String id, String otherId) {
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(id));
        UserValidator.isUserByUsers(userStorage.getUsers(), userStorage.findUserById(otherId));

        List<User> result = new ArrayList<>();

        if (userStorage.findUserById(otherId).getFriends() != null || userStorage.findUserById(id).getFriends() != null) {
            for (Integer friendOtherId : userStorage.findUserById(otherId).getFriends()) {
                for (Integer friendId : userStorage.findUserById(id).getFriends()) {
                    if (friendOtherId.equals(friendId)) {
                        result.add(userStorage.findUserById(String.valueOf(friendId)));
                    }
                }
            }
        }

        return result;
    }
}