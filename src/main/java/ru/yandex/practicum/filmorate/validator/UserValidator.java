package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class UserValidator {
    /**
     * Валидация имени пользователей
     */
    public static void isValidNameUsers(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email не должен быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email должен содержать - \"@\"");
        }
        if (user.getLogin().isBlank()) {
            log.warn("Ошибка в логине: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "логин не должен быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пусто - заменено логином: {}", user);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в дате рождения: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "дата рождения не может быть в будущем");
        }
    }

    /**
     * Валидация id пользователей
     */
    public static void isValidIdUsers(int id) {
        if (id < 0) {
            throw new NotFoundUserException(String.format("Id пользователя {} отрицательный", id));
        }
    }

    public static void isUserByUsers(List<User> users, User user) {
        if (!users.contains(user)) {
            throw new NotFoundUserException(String.format("Пользователь № %d не найден", user.getId()));
        }
    }
}
