package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundMpaException extends RuntimeException{
    public NotFoundMpaException(final String message) {
        super(message);
    }
}
