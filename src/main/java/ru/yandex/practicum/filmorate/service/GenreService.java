package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;
import java.util.Set;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * возвращает список жанров.
     */
    public Set<Genre> getGenresService() {
        return genreDao.getGenres();
    }

    /**
     * возвращает жанр по id.
     */
    public Optional<Genre> getGenresFromFilmService(String igFilm) {
        return genreDao.getGenresFromFilm(igFilm);
    }
}
