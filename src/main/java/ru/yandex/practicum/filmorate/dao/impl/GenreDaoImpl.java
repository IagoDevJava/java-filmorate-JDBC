package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validator.GenreValidator;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Component
public class GenreDaoImpl implements GenreDao {
    private final Logger log = LoggerFactory.getLogger(GenreDao.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * возвращает список жанров.
     */
    @Override
    public Set<Genre> getGenres() {
        Set<Genre> genres = new TreeSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE");
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(Objects.requireNonNull(genreRows.getString("name")))
                    .build();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            if (genre.getId() > 0) {
                genres.add(genre);
            }
        }
        return genres;
    }

    /**
     * возвращает жанр по id.
     */
    @Override
    public Optional<Genre> getGenresFromFilm(String idGenre) {
        GenreValidator.isValidIdGenre(Integer.parseInt(idGenre));
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT *\n" +
                        "FROM GENRE\n" +
                        "WHERE ID=?", idGenre);
        if (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"))
                    .build();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("жанр с идентификатором {} не найден.", idGenre);
            throw new NotFoundGenreException("Такого MPA нет в базе.");
        }
    }
}
