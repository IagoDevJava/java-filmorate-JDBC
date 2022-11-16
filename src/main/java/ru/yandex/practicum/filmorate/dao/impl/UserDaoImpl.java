package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * получение списка пользователей
     */
    @Override
    public List<User> getUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS");
        log.info("Получили список пользователей");
        return getUsers(allUsers, userRows);
    }

    /**
     * создание пользователя
     */
    @Override
    public User createUser(User user) {
        UserValidator.isValidNameUsers(user);
        try (Connection con = DriverManager.getConnection(
                "jdbc:h2:file:./db/filmorate", "sa", "password");) {
            String sql =
                    "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY) VALUES ((?), (?), (?), (?))";
            final PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, String.valueOf(user.getBirthday()));
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Connection failed...\n" + ex);
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where LOGIN=?", user.getLogin());
        if (userRows.next()) {
            user.setId(userRows.getInt("id"));
        }
        log.info("Создан пользователь: {} {}", user.getId(), user.getLogin());
        return user;
    }

    /**
     * обновление пользователя
     */
    @Override
    public User updateUser(User user) {
        UserValidator.isValidNameUsers(user);
        if (findUserById(String.valueOf(user.getId())).isPresent()) {
            try (Connection con = DriverManager.getConnection(
                    "jdbc:h2:file:./db/filmorate", "sa", "password");) {
                String sql =
                        "UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? " +
                                "WHERE ID = ?";
                final PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, String.valueOf(user.getBirthday()));
                preparedStatement.setInt(5, user.getId());
                preparedStatement.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Connection failed...\n" + ex);
            }
            User userUpdate = User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .login(user.getLogin())
                    .email(user.getEmail())
                    .birthday(user.getBirthday())
                    .build();
            log.info("Обновлен пользователь: {} {}", user.getId(), user.getLogin());
            return userUpdate;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            throw new NotFoundUserException("Такого пользователя нет в базе.");
        }
    }

    /**
     * найти пользователя по id
     */
    @Override
    public Optional<User> findUserById(String idStr) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where ID=?", idStr);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getInt("id"))
                    .name(userRows.getString("name"))
                    .login(Objects.requireNonNull(userRows.getString("login")))
                    .email(Objects.requireNonNull(userRows.getString("email")))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", idStr);
            throw new NotFoundUserException("Такого пользователя нет в базе.");
        }
    }

    /**
     * добавление в друзья
     */
    @Override
    public void addFriends(String idUserStr, String idFriendStr) {
        int idUser = Integer.parseInt(idUserStr);
        int idFriend = Integer.parseInt(idFriendStr);
        if (idUser > 0 && idFriend > 0) {
            try (Connection con = DriverManager.getConnection(
                    "jdbc:h2:file:./db/filmorate", "sa", "password");) {
                String sql =
                        "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES ((?), (?))";
                final PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idUser);
                preparedStatement.setInt(2, idFriend);
                preparedStatement.execute();
            } catch (Exception ex) {
                System.out.println("Connection failed...\n" + ex);
            }
            log.info("Пользователь {} добавлен в подписки к пользователю {}", idFriend, idUser);
        } else {
            log.info("Id friend - {}, Id user - {}. Отрицательные значение не допустимы.", idFriend, idUser);
            throw new NotFoundUserException("Ошибка id");
        }
    }

    /**
     * удаление из друзей
     */
    @Override
    public void deleteFriend(String idUser, String idFriend) {
        if (findUserById(idUser).isPresent() && findUserById(idFriend).isPresent()) {
            String sql = "DELETE FROM FRIENDSHIP WHERE FRIEND_ID=? AND USER_ID=?";
            jdbcTemplate.update(sql, idFriend, idUser);
        } else {
            throw new NotFoundUserException("Такого пользователя нет в базе.");
        }
        log.info("Пользователь {} удален из подписок пользователя {}", idFriend, idUser);
    }

    /**
     * возвращаем список друзей пользователя
     */
    @Override
    public List<User> getFriendsUser(String idStr) {
        UserValidator.isValidIdUsers(Integer.parseInt(idStr));
        try {
            String sql = "SELECT USERS.ID, USERS.NAME, USERS.LOGIN, USERS.EMAIL, USERS.BIRTHDAY " +
                    "FROM USERS AS U " +
                    "LEFT JOIN FRIENDSHIP F on U.ID = F.USER_ID " +
                    "LEFT JOIN USERS on F.FRIEND_ID = USERS.ID " +
                    "WHERE U.ID=?";
            List<User> friends = new ArrayList<>();
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, idStr);
            getUsers(friends, userRows);
            log.info("У пользователя с id {} в подписках {} друзей", idStr, friends.size());
            return friends;
        } catch (Exception e) {
            log.info("У пользователя с id {} в подписках нет друзей", idStr);
            return new ArrayList<>();
        }
    }

    /**
     * вывод списка общих друзей
     */
    @Override
    public List<User> getListCommonFriendsDao(String id, String otherId) {
        UserValidator.isValidIdUsers(Integer.parseInt(id));
        UserValidator.isValidIdUsers(Integer.parseInt(otherId));
        try {
            String sql = "SELECT U.ID, U.EMAIL, U.NAME, U.LOGIN, U.BIRTHDAY " +
                    "FROM FRIENDSHIP FR " +
                    "LEFT JOIN USERS U on U.ID = FR.FRIEND_ID " +
                    "WHERE USER_ID = ? " +
                    "   OR USER_ID = ? " +
                    "GROUP BY U.ID, U.NAME, U.NAME, U.LOGIN, U.BIRTHDAY " +
                    "HAVING COUNT(FRIEND_ID) > 1";
            List<User> commonFriends = new ArrayList<>();
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id, otherId);
            getUsers(commonFriends, userRows);
            log.info("У пользователей с id {} {} общих друзей: {} друзей", id, otherId, commonFriends.size());
            return commonFriends;
        } catch (Exception e) {
            log.info("У пользователей с id {} и {} в нет общих друзей", id, otherId);
            return new ArrayList<>();
        }
    }

    /**
     * Удаление пользователей из списка
     */
    @Override
    public void clearUsers() {
        String sqlDropFr = "DELETE FROM FRIENDSHIP";
        jdbcTemplate.update(sqlDropFr);
        String sqlDelUsers = "DELETE from USERS";
        jdbcTemplate.update(sqlDelUsers);
        log.info("Удалены все пользователи таблицы USERS");

    }

    /**
     * Удаление пользователя по id
     */
    @Override
    public void deleteUserById(int id) {
        if (findUserById(String.valueOf(id)).isPresent()) {
            String sqlDelFr = "DELETE FROM FRIENDSHIP WHERE USER_ID=? OR FRIEND_ID=?";
            jdbcTemplate.update(sqlDelFr, id, id);
            String sqlDelUs = "DELETE from USERS where ID=?";
            jdbcTemplate.update(sqlDelUs, id);
        } else {
            throw new NotFoundUserException("Такого пользователя нет в базе.");
        }
        log.info("Удален пользователь: {}", id);
    }

    /**
     * Получение списка пользователей из таблицы
     */
    private List<User> getUsers(List<User> users, SqlRowSet userRows) {
        while (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getInt("id"))
                    .name(userRows.getString("name"))
                    .login(Objects.requireNonNull(userRows.getString("login")))
                    .email(Objects.requireNonNull(userRows.getString("email")))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
            users.add(user);
        }
        return users;
    }
}
