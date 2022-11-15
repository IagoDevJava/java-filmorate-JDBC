package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    /**
     * добавление фильма в список
     */
    Film addFilm(Film film);

    /**
     * обновление фильма в списке
     */
    Film updateFilm(Film film);

    /**
     * Удаление фильмов из списка
     */
    void clearFilms();

    /**
     * Удаление фильма по id
     */
    void deleteFilmById(String idStr);

    /**
     * получение всех фильмов
     */
    List<Film> getFilms();

    /**
     * получение фильма по id
     */
    Optional<Film> findFilmById(String idStr);

    /**
     * Пользователь ставит фильму лайк
     */
    void addLikeFilms(String idFilm, String idUser);

    /**
     * пользователь удаляет лайк.
     */
    void deleteLikeFilm(String idFilm, String idUser);

    /**
     * возвращает список первых фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */
    List<Film> getPopularFilms(String countStr);
}
