package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;
import java.util.Set;

public interface GenreDao {

    /**
     * возвращает список жанров.
     */
    Set<Genre> getGenres();

    /**
     * возвращает список жанров у фильма.
     */
    Optional<Genre> getGenresFromFilm(String idFilm);
}
