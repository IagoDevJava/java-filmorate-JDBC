package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exeptions.NotFoundGenreException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;

public class GenreValidator {
    /**
     * Валидация id жанра
     */
    public static void isValidIdGenre(int id) throws ValidationException {
        if (id < 0) {
            throw new NotFoundGenreException(String.format("Id жанра %d отрицательный", id));
        }
    }
}
