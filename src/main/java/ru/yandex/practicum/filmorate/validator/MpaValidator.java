package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exeptions.NotFoundMpaException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;

public class MpaValidator {

    /**
     * Валидация id MPA
     */
    public static void isValidIdMpa(int id) throws ValidationException {
        if (id < 0) {
            throw new NotFoundMpaException(String.format("Id MPA %d отрицательный", id));
        }
    }
}
