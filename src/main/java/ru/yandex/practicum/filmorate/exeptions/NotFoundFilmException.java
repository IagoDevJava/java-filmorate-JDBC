package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundFilmException extends RuntimeException{
    public NotFoundFilmException(String message) {
        super(message);
    }
}
