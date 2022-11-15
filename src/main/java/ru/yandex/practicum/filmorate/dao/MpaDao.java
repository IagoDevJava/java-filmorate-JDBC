package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    /**
     * возвращает список MPA.
     */
    List<Mpa> getMpa();

    /**
     * возвращает MPA у фильма.
     */
    Optional<Mpa> getMpaFromFilm(String idFilm);
}
