package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundGenreException extends RuntimeException {
    public NotFoundGenreException(final String message) {
        super(message);
    }
}
