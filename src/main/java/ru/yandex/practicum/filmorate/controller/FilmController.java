package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * получение всех фильмов
     */
    @GetMapping
    public List<Film> getFilms() {
        log.debug("Получили список всех фильмов.");
        return filmService.getFilms();
    }

    /**
     * добавление фильма в список
     */
    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        log.debug("Добавляем: {}", film);
        return filmService.addFilm(film);
    }

    /**
     * обновление фильма в списке
     */
    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Обновляем: {}", film);
        return filmService.updateFilm(film);
    }

    /**
     * Очистить список фильмов
     */
    @DeleteMapping
    public void clearFilms() {
        log.debug("Очищаем список фильмов.");
        filmService.clearFilms();
    }

    /**
     * Удаление фильма по id
     */
    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable String id) {
        log.debug("Удаляем фильм по id {}.", id);
        filmService.deleteFilmById(id);
    }

    /**
     * получение фильма по id
     */
    @GetMapping("/{id}")
    public Optional<Film> findFilmById(@PathVariable String id) {
        log.debug("Получаем фильм по id {}", id);
        return filmService.findFilmById(id);
    }

    /**
     * Пользователь ставит фильму лайк
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable String id, @PathVariable String userId) {
        log.debug("Пользователь с id {} ставит лайк фильму с id {}", userId, id);
        filmService.addLikeFilms(id, userId);
    }

    /**
     * пользователь удаляет лайк.
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable String id, @PathVariable String userId) {
        log.debug("Пользователь с id {} удаляет лайк к фильму с id {}", userId, id);
        filmService.deleteLikeFilm(id, userId);
    }

    /**
     * возвращает список фильмов по количеству лайков.
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) String count) {
        log.debug("Возвращаем популярные фильмы");
        return filmService.getPopularFilms(Objects.requireNonNullElse(count, "10"));
    }
}