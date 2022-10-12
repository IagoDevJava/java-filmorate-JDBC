package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    /**
     * получение всех фильмов
     */
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    /**
     * добавление фильма в список
     */
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    /**
     * обновление фильма в списке
     */
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    /**
     * Очистить список фильмов
     */
    public void clearFilms() {
        filmStorage.clearFilms();
    }

    /**
     * Удаление фильма по id
     */
    public void deleteFilmById(String idStr) {
        filmStorage.deleteFilmById(idStr);
    }

    /**
     * получение фильма по id
     */
    public Film findFilmById(String idStr) {
        return filmStorage.findFilmById(idStr);
    }

    /**
     * Пользователь ставит фильму лайк
     */
    public void addLikeFilms(String idFilm, String idUser) {
        int userId = Integer.parseInt(idUser);
        int filmId = Integer.parseInt(idFilm);
        FilmValidator.isValidIdFilms(filmId);
        UserValidator.isValidIdUsers(userId);
        FilmValidator.isFilmByFilms(filmStorage.getFilms(), findFilmById(idFilm));

        if (isLikesByFilm(idFilm)) {
            Set<Integer> likes = findFilmById(idFilm).getUsersLike();
            likes.add(userId);
            findFilmById(idFilm).setUsersLike(likes);
        } else {
            TreeSet<Integer> likes = new TreeSet<>();
            likes.add(userId);
            findFilmById(idFilm).setUsersLike(likes);
        }
    }

    /**
     * проверка наличия списка лайков
     */
    private Boolean isLikesByFilm(String id) {
        return filmStorage.findFilmById(id).getUsersLike() != null;
    }

    /**
     * пользователь удаляет лайк.
     */
    public void deleteLikeFilm(String idFilm, String idUser) {
        int userId = Integer.parseInt(idUser);
        int filmId = Integer.parseInt(idFilm);
        FilmValidator.isValidIdFilms(filmId);
        UserValidator.isValidIdUsers(userId);
        FilmValidator.isFilmByFilms(filmStorage.getFilms(), findFilmById(idFilm));

        if (isLikesByFilm(idFilm)) {
            Set<Integer> likes = findFilmById(idFilm).getUsersLike();
            likes.remove(userId);
            findFilmById(idFilm).setUsersLike(likes);
        } else {
            throw new NotFoundUserException(String.format("У фильма № %d нет лайков", filmId));
        }
    }

    /**
     * возвращает список первых фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */
    public List<Film> getPopularFilms(String countStr) {
        int count = Integer.parseInt(countStr);
        if (filmStorage.getFilms() == null) {
            throw new NotFoundFilmException("Список фильмов пуст.");
        }
        return filmStorage.getFilms().stream()
                .sorted(this::compareFilmsReverse)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compareFilmsReverse(Film f1, Film f2) {
        int comp1 = 0;
        int comp2 = 0;
        if (f1.getUsersLike() != null) {
            comp1 = f1.getUsersLike().size();
        }
        if (f2.getUsersLike() != null) {
            comp2 = f2.getUsersLike().size();
        }
        return comp2 - comp1;
    }
}