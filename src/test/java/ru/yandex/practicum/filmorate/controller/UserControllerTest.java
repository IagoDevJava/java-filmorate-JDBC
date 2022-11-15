package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
//    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserDaoImpl userDao = new UserDaoImpl(new JdbcTemplate());
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);
    User expectedUser;
    User expectedUser1;

    @Test
    void isValidUser() throws ValidationException, SQLException {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.createUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser, actualUser, "Пользователь не добавлен");
    }

    @Test
    void isValidEmailUserBlank() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email(" ")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        expectedUser1 = User.builder()
                .name("user")
                .login("login")
                .email("")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.createUser(expectedUser);
            userController.createUser(expectedUser1);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidEmailWithSpecialSymbol() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("useryandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.createUser(expectedUser);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidLoginUserBlank() {
        expectedUser = User.builder()
                .name("user")
                .login("")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        expectedUser1 = User.builder()
                .name("user")
                .login(" ")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.createUser(expectedUser);
            userController.createUser(expectedUser1);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidNameUserBlank() throws ValidationException, SQLException {
        expectedUser = User.builder()
                .name(" ")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.createUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidNameUserNull() throws ValidationException, SQLException {
        expectedUser = User.builder()
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.createUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidNameUserEmpty() throws ValidationException, SQLException {
        expectedUser = User.builder()
                .name("")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.createUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidBirthdayUser() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.createUser(expectedUser);
        });
        assertNotNull(thrown.getMessage());
    }
}