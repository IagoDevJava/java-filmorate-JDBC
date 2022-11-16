package ru.yandex.practicum.filmorate.Dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.exeptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoImplTest {
    private final FilmDaoImpl filmDao;

    /**
     * тест на получение всех фильмов
     */
    @Test
    void isGetFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        List<Film> expected = new ArrayList<>();
        expected.add(film1);

        List<Film> actual = filmDao.getFilms();

        assertEquals(expected, actual, "Списки фильмов не совпадают");
    }

    /**
     * тест на получение фильма по id
     */
    @Test
    public void isFindUserById() {
        Optional<Film> filmOptional = filmDao.findFilmById(String.valueOf(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    /**
     * тест на обновление фильма
     */
    @Test
    void isUpdateUser() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDao.addFilm(film1);
        Film film2 = Film.builder()
                .id(1)
                .name("film2")
                .description("descr2")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDao.addFilm(film2);

        Film actualFilm = filmDao.updateFilm(film2);

        assertEquals(film2, actualFilm, "Фильмы не совпадают");
    }

    /**
     * Пользователь ставит фильму лайк, возвращаем популярные фильмы
     */
    @Test
    void isAddLike() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("film2")
                .description("descr2")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("2")
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        User user1 = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        User user2 = User.builder()
                .id(2)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        filmDao.addLikeFilms(String.valueOf(film1.getId()), String.valueOf(user1.getId()));
        filmDao.addLikeFilms(String.valueOf(film2.getId()), String.valueOf(user1.getId()));
        filmDao.addLikeFilms(String.valueOf(film2.getId()), String.valueOf(user2.getId()));
        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);

        List<Film> popularFilms = filmDao.getPopularFilms("10");

        assertEquals(expected, popularFilms, "Списки не совпадают");
    }

    /**
     * тест на удаление фильма по id
     */
    @Test
    public void isDeleteUserById() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        filmDao.addFilm(film1);

        filmDao.deleteFilmById(String.valueOf(film1.getId()));

        assertThrows(NotFoundFilmException.class, () -> {
            filmDao.findFilmById("1");
        });
    }

    /**
     * Тест на удаление фильмов из списка
     */
    @Test
    void isClearUsers() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();
        filmDao.addFilm(film1);
        List<Film> expectedList = new ArrayList<>();

        filmDao.clearFilms();
        List<Film> actualListFilms = filmDao.getFilms();

        assertEquals(expectedList, actualListFilms, "Список не пуст");
    }
}
