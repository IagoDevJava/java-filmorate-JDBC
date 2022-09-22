package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    /**
     * Хранение списка добавленных фильмов
     */
    private final Set<Film> films = new HashSet<>();

    /**
     * получение всех фильмов
     */
    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films);
    }

    /**
     * добавление фильма в список
     */
    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        isValidFilms(film);
        log.debug("Сохранили: {}", film);
        films.add(film);
        return film;
    }

    /**
     * обновление фильма в списке
     */
    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        isValidFilms(film);
        log.debug("Обновили: {}", film);
        films.add(film);
        return film;
    }

    /**
     * Валидация фильмов
     */
    protected void isValidFilms(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Ошибка в названии: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "название не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка в описании: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "длина описания не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-27"))) {
            log.warn("Ошибка в дате релиза: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            log.warn("Ошибка в продолжительности: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "продолжительность фильма не может быть отрицательной");
        }
    }
}
