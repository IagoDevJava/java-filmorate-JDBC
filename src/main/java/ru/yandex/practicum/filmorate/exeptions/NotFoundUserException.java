package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(final String message) {
        super(message);
    }
}
