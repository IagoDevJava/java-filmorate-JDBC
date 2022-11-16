package ru.yandex.practicum.filmorate.Dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoImplTest {
    private final UserDaoImpl userDao;

    /**
     * тест на получение пользователя по id
     */
    @Test
    public void isFindUserById() {
        Optional<User> userOptional = userDao.findUserById(String.valueOf(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    /**
     * тест на получение списка пользователей
     */
    @Test
    void isGetUsers() {
        User user1 = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        User user2 = User.builder()
                .id(2)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        User user3 = User.builder()
                .id(3)
                .name("user3")
                .login("login3")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user1);
        expectedList.add(user2);
        expectedList.add(user3);

        List<User> actualListUsers = userDao.getUsers();

        assertEquals(expectedList, actualListUsers, "Списки пользователей не совпадают");
    }

    /**
     * тест на обновление пользователя
     */
    @Test
    void isUpdateUser() {
        User expectedUser = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUser);
        User expectedUpdateUser = User.builder()
                .id(1)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUpdateUser);

        User actualUser = userDao.updateUser(expectedUpdateUser);

        assertEquals(expectedUpdateUser, actualUser, "Пользователи не совпадают");
    }

    /**
     * тест на добавление в друзья и получение списка друзей
     */
    @Test
    public void isAddFriends() {
        User expectedUser = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUser);
        User expectedFriend = User.builder()
                .id(2)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedFriend);
        List<User> expected = new ArrayList<>();
        expected.add(expectedFriend);

        userDao.addFriends(String.valueOf(expectedUser.getId()), String.valueOf(expectedFriend.getId()));
        List<User> actual = userDao.getFriendsUser(String.valueOf(expectedUser.getId()));

        assertEquals(expected, actual, "Списки друзей не совпадают");
    }

    /**
     * тест на вывод списка общих друзей
     */
    @Test
    void isGetListCommonFriends() {
        User user1 = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user1);
        User user2 = User.builder()
                .id(2)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user2);
        User user3 = User.builder()
                .id(3)
                .name("user3")
                .login("login3")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user3);
        List<User> expected = new ArrayList<>();
        expected.add(user2);

        userDao.addFriends("1", "2");
        userDao.addFriends("3", "2");
        List<User> actual = userDao.getListCommonFriendsDao("1", "3");

        assertEquals(expected, actual, "Списки друзей не совпадают");
    }

    /**
     * тест на удаление пользователя по id
     */
    @Test
    public void isDeleteUserById() {
        User expectedUser = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUser);

        userDao.deleteUserById(expectedUser.getId());

        assertThrows(NotFoundUserException.class, () -> {
            userDao.findUserById("1");
        });
    }

    /**
     * Тест на удаление пользователей из списка
     */
    @Test
    void isClearUsers() {
        User expectedUser = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUser);
        List<User> expectedList = new ArrayList<>();

        userDao.clearUsers();
        List<User> actualListUsers = userDao.getUsers();

        assertEquals(expectedList, actualListUsers, "Список не пуст");
    }
}
