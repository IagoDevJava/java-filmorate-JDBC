package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    UserDaoImpl userDao = new UserDaoImpl(new JdbcTemplate());
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);
    User expectedUser;
    User expectedUser1;

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