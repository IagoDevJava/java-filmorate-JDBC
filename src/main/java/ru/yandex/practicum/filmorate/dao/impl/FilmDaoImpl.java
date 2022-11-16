package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundMpaException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final Logger log = LoggerFactory.getLogger(FilmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * получение всех фильмов
     */
    @Override
    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM");
        return getFilms(films, filmRows);
    }

    /**
     * добавление фильма в список
     */
    @Override
    public Film addFilm(Film film) {
        FilmValidator.isValidFilms(film);
        try (Connection con = DriverManager.getConnection(
                "jdbc:h2:file:./db/filmorate", "sa", "password");) {

            String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA) "
                    + "VALUES ((?), (?), (?), (?), (?), (?))";
            final PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setString(3, String.valueOf(film.getReleaseDate()));
            preparedStatement.setString(4, String.valueOf(film.getDuration()));
            preparedStatement.setString(5, film.getRate());
            preparedStatement.setInt(6, film.getMpa().getId());
            preparedStatement.executeUpdate();

            SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where NAME=?", film.getName());
            if (filmRows.next()) {
                film.setId(filmRows.getInt("id"));
            }

            if (film.getGenres() != null) {
                String sqlG = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
                final PreparedStatement preparedStatement1 = con.prepareStatement(sqlG);
                for (Genre genre : film.getGenres()) {
                    preparedStatement1.setInt(1, film.getId());
                    preparedStatement1.setInt(2, genre.getId());
                    preparedStatement1.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...\n" + ex);
        }
        log.info("Создан фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    /**
     * обновление фильма в списке
     */
    @Override
    public Film updateFilm(Film film) {
        FilmValidator.isValidFilms(film);
        if (findFilmById(String.valueOf(film.getId())).isPresent()) {
            try (Connection con = DriverManager.getConnection(
                    "jdbc:h2:file:./db/filmorate", "sa", "password");) {
                String sql = "UPDATE FILM SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATE=?, MPA=?"
                        + " WHERE ID = ?";
                final PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, film.getName());
                preparedStatement.setString(2, film.getDescription());
                preparedStatement.setString(3, String.valueOf(film.getReleaseDate()));
                preparedStatement.setString(4, String.valueOf(film.getDuration()));
                preparedStatement.setString(5, film.getRate());
                preparedStatement.setInt(6, film.getMpa().getId());
                preparedStatement.setInt(7, film.getId());

                if (film.getGenres() != null) {
                    if (!film.getGenres().isEmpty()) {
                        String sqlGD = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                        jdbcTemplate.update(sqlGD, film.getId());

                        String sqlG = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
                        final PreparedStatement preparedStatement1 = con.prepareStatement(sqlG);
                        for (Genre genre : film.getGenres()) {
                            preparedStatement1.setInt(1, film.getId());
                            preparedStatement1.setInt(2, genre.getId());
                            preparedStatement1.execute();
                        }
                    } else {
                        String sqlG = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                        jdbcTemplate.update(sqlG, film.getId());
                    }
                }
                preparedStatement.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Connection failed...\n" + ex);
            }
            Film filmUpdate = Film.builder()
                    .id(film.getId())
                    .name(film.getName())
                    .description(film.getDescription())
                    .releaseDate(film.getReleaseDate())
                    .duration(film.getDuration())
                    .rate(film.getRate())
                    .mpa(film.getMpa())
                    .genres(checkGenreToNull(film.getGenres()))
                    .build();
            log.info("Обновлен фильм: {} {}", film.getId(), film.getName());
            return filmUpdate;
        } else {
            log.info("фильм с идентификатором {} не найден.", film.getId());
            throw new NotFoundFilmException("Такого фильма нет в базе.");
        }
    }

    /**
     * Удаление фильмов из списка
     */
    @Override
    public void clearFilms() {
        String sqlDelLikes = "DELETE FROM LIKES";
        jdbcTemplate.update(sqlDelLikes);
        String sql = "DELETE from FILM";
        jdbcTemplate.update(sql);
        log.info("Удалены все фильмы таблицы FILM");
    }

    /**
     * Удаление фильма по id
     */
    @Override
    public void deleteFilmById(String idStr) {
        if (findFilmById(idStr).isPresent()) {
            String sqlDelLikesId = "DELETE FROM LIKES WHERE FILM_ID=?";
            jdbcTemplate.update(sqlDelLikesId, idStr);
            String sql = "DELETE from FILM where ID=?";
            jdbcTemplate.update(sql, idStr);
        } else {
            throw new NotFoundFilmException("Такого фильма нет в базе.");
        }
        log.info("Удален фильм: {}", idStr);
    }

    /**
     * получение фильма по id
     */
    @Override
    public Optional<Film> findFilmById(String idStr) {
        FilmValidator.isValidIdFilms(Integer.parseInt(idStr));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where ID=?", idStr);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("id"))
                    .name(Objects.requireNonNull(filmRows.getString("name")))
                    .description(Objects.requireNonNull(filmRows.getString("description")))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .rate(filmRows.getString("rate"))
                    .mpa(getMpa(filmRows.getInt("MPA")))
                    .genres(getGenresForFilm(filmRows.getString("id")))
                    .build();
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", idStr);
            throw new NotFoundFilmException("Такого фильма нет в базе.");
        }
    }

    /**
     * Пользователь ставит фильму лайк
     */
    @Override
    public void addLikeFilms(String idFilm, String idUser) {
        try (Connection con = DriverManager.getConnection(
                "jdbc:h2:file:./db/filmorate", "sa", "password");) {
            String sql = "INSERT INTO LIKES(USER_ID, FILM_ID) VALUES ((?), (?))";
            final PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(idUser));
            preparedStatement.setInt(2, Integer.parseInt(idFilm));
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Connection failed...\n" + ex);
        }
        log.info("Пользователь {} поставил лайк к фильму {}", idUser, idFilm);
    }

    /**
     * пользователь удаляет лайк.
     */
    @Override
    public void deleteLikeFilm(String idFilm, String idUser) {
        UserValidator.isValidIdUsers(Integer.parseInt(idUser));
        FilmValidator.isValidIdFilms(Integer.parseInt(idFilm));
        if (findFilmById(idFilm).isPresent()) {
            String sql = "DELETE FROM LIKES WHERE USER_ID=? AND FILM_ID=?";
            jdbcTemplate.update(sql, idUser, idFilm);
        } else {
            throw new NotFoundFilmException("Такого фильма нет в базе.");
        }
        log.info("Удален фильм: {}", idFilm);
    }

    /**
     * возвращает список первых фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */
    @Override
    public List<Film> getPopularFilms(String countStr) {
        int count;
        if (countStr == null) {
            count = 10;
        } else {
            count = Integer.parseInt(countStr);
        }
        List<Film> popularFilms = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATE, F.MPA "
                        + "FROM FILM F LEFT JOIN LIKES L on F.ID = L.FILM_ID GROUP BY F.NAME "
                        + "ORDER BY COUNT(L.USER_ID) DESC LIMIT ?", count);
        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("id"))
                    .name(Objects.requireNonNull(filmRows.getString("name")))
                    .description(Objects.requireNonNull(filmRows.getString("description")))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .rate(filmRows.getString("rate"))
                    .duration(filmRows.getInt("duration"))
                    .mpa(getMpa(filmRows.getInt("MPA")))
                    .build();
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            popularFilms.add(film);
        }
        log.info("Создали список популярных фильмов, в количестве: {}", popularFilms.size());
        return popularFilms;
    }

    private List<Film> getFilms(List<Film> popularFilms, SqlRowSet filmRows) {
        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("id"))
                    .name(Objects.requireNonNull(filmRows.getString("name")))
                    .description(Objects.requireNonNull(filmRows.getString("description")))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .rate(filmRows.getString("rate"))
                    .mpa(getMpa(filmRows.getInt("MPA")))
                    .genres(getGenresForFilm(filmRows.getString("id")))
                    .build();
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            popularFilms.add(film);
        }
        return popularFilms;
    }

    private Mpa getMpa(int idMpa) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "select * from MPA where ID=?", idMpa);
        Mpa mpa;
        if (mpaRows.next()) {
            mpa = Mpa.builder()
                    .id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"))
                    .build();
        } else {
            throw new NotFoundMpaException("MPA не найден");
        }
        return mpa;
    }

    private TreeSet<Genre> getGenresForFilm(String idFilm) {
        FilmValidator.isValidIdFilms(Integer.parseInt(idFilm));
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT G2.ID, G2.NAME "
                        + "FROM FILM F "
                        + "LEFT JOIN FILM_GENRE FG on F.ID = FG.film_id "
                        + "LEFT JOIN GENRE G2 on FG.genre_id = G2.ID "
                        + "WHERE F.ID =?", idFilm);
        TreeSet<Genre> genresSet = new TreeSet<>();
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"))
                    .build();
            if (genre.getId() > 0) {
                genresSet.add(genre);
            }
        }
        return genresSet;
    }

    private TreeSet<Genre> checkGenreToNull(TreeSet<Genre> genres) {
        boolean check = true;
        if (genres != null) {
            if (!genres.isEmpty()) {
                for (Genre genre : genres) {
                    check = genre.getId() > 0;
                }
                if (!check) {
                    genres.clear();
                }
            }
        }
        return genres;
    }

}