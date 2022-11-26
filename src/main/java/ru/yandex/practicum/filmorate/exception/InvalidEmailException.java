package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
