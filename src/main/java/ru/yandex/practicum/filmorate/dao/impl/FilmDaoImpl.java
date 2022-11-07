package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public class FilmDaoImpl implements FilmDao {
    /**
     * добавление фильма в список
     */
    @Override
    public Film addFilm(Film film) {
        return null;
    }

    /**
     * обновление фильма в списке
     */
    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    /**
     * Удаление фильмов из списка
     */
    @Override
    public void clearFilms() {

    }

    /**
     * Удаление фильма по id
     */
    @Override
    public void deleteFilmById(String idStr) {

    }

    /**
     * получение всех фильмов
     */
    @Override
    public List<Film> getFilms() {
        return null;
    }

    /**
     * получение фильма по id
     */
    @Override
    public Film findFilmById(String idStr) {
        return null;
    }
}
