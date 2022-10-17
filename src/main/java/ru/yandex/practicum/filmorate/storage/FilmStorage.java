package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
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
    Film findFilmById(String idStr);

}
