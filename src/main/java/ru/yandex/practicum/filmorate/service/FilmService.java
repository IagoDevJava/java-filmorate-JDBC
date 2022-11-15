package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmDao filmDao;

    @Autowired
    public FilmService(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    /**
     * получение всех фильмов
     */
    public List<Film> getFilms() {
        return filmDao.getFilms();
    }

    /**
     * добавление фильма в список
     */
    public Film addFilm(Film film) {
        return filmDao.addFilm(film);
    }

    /**
     * обновление фильма в списке
     */
    public Film updateFilm(Film film) {
        return filmDao.updateFilm(film);
    }

    /**
     * Очистить список фильмов
     */
    public void clearFilms() {
        filmDao.clearFilms();
    }

    /**
     * Удаление фильма по id
     */
    public void deleteFilmById(String idStr) {
        filmDao.deleteFilmById(idStr);
    }

    /**
     * получение фильма по id
     */
    public Optional<Film> findFilmById(String idStr) {
        return filmDao.findFilmById(idStr);
    }

    /**
     * Пользователь ставит фильму лайк
     */
    public void addLikeFilms(String idFilm, String idUser) {
        filmDao.addLikeFilms(idFilm, idUser);
    }

    /**
     * пользователь удаляет лайк.
     */
    public void deleteLikeFilm(String idFilm, String idUser) {
        filmDao.deleteLikeFilm(idFilm, idUser);
    }

    /**
     * возвращает список первых фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */
    public List<Film> getPopularFilms(String countStr) {
        return filmDao.getPopularFilms(countStr);
    }
}