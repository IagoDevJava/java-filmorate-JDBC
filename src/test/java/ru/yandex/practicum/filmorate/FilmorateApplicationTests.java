package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exeptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundUserException;
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
//@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDaoImpl userDao;
    private final FilmDaoImpl filmDao;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void initEach() {
        user1 = User.builder()
                .id(1)
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user1);
        user2 = User.builder()
                .id(2)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user2);
        user3 = User.builder()
                .id(3)
                .name("user3")
                .login("login3")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(user3);

        film1 = Film.builder()
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
        film2 = Film.builder()
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
    }

    @AfterEach
    public void clearEach() {
        userDao.clearUsers();
        filmDao.clearFilms();
    }

    /**
     * тест на получение пользователя по id
     */
    @Test
    public void isFindUserById() {
        Optional<User> userOptional = userDao.findUserById(String.valueOf(user1.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user1.getId())
                );
    }

    /**
     * тест на получение списка пользователей
     */
    @Test
    void isGetUsers() {
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user1);
        expectedList.add(user2);
        expectedList.add(user3);

        List<User> actualListUsers = userDao.getUsers();

        assertEquals(expectedList, actualListUsers, "Списки пользователей не совпадают");
    }

    /**
     * тест на обновление пользователя
     */
    @Test
    void isUpdateUser() {
        User expectedUpdateUser = User.builder()
                .id(1)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDao.createUser(expectedUpdateUser);

        User actualUser = userDao.updateUser(expectedUpdateUser);

        assertEquals(expectedUpdateUser, actualUser, "Пользователи не совпадают");
    }

    /**
     * тест на добавление в друзья и получение списка друзей
     */
    @Test
    public void isAddFriends() {
        List<User> expected = new ArrayList<>();
        expected.add(user2);

        userDao.addFriends(String.valueOf(user1.getId()), String.valueOf(user2.getId()));
        List<User> actual = userDao.getFriendsUser(String.valueOf(user1.getId()));

        assertEquals(expected, actual, "Списки друзей не совпадают");
    }

    /**
     * тест на вывод списка общих друзей
     */
    @Test
    void isGetListCommonFriends() {
        List<User> expected = new ArrayList<>();
        expected.add(user2);

        userDao.addFriends(String.valueOf(user1.getId()), String.valueOf(user2.getId()));
        userDao.addFriends(String.valueOf(user3.getId()), String.valueOf(user2.getId()));
        List<User> actual = userDao.getListCommonFriendsDao(String.valueOf(user1.getId()), String.valueOf(user3.getId()));

        assertEquals(expected, actual, "Списки друзей не совпадают");
    }

    /**
     * тест на удаление пользователя по id
     */
    @Test
    public void isDeleteUserById() {
        userDao.deleteUserById(user1.getId());

        assertThrows(NotFoundUserException.class, () -> {
            userDao.findUserById(String.valueOf(user1.getId()));
        });
    }

    /**
     * Тест на удаление пользователей из списка
     */
    @Test
    void isClearUsers() {
        List<User> expectedList = new ArrayList<>();

        userDao.clearUsers();
        List<User> actualListUsers = userDao.getUsers();

        assertEquals(expectedList, actualListUsers, "Список не пуст");
    }

    /**
     * тест на получение всех фильмов
     */
    @Test
    void isGetFilms() {
        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);

        List<Film> actual = filmDao.getFilms();

        assertEquals(expected, actual, "Списки фильмов не совпадают");
    }

    /**
     * тест на получение фильма по id
     */
    @Test
    public void isFindFilmById() {
        Optional<Film> filmOptional = filmDao.findFilmById(String.valueOf(film1.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", film1.getId())
                );
    }

    /**
     * тест на обновление фильма
     */
    @Test
    void isUpdateFilm() {
        Film filmUpd = Film.builder()
                .id(1)
                .name("film2")
                .description("descr2")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDao.addFilm(filmUpd);

        Film actualFilm = filmDao.updateFilm(filmUpd);

        assertEquals(filmUpd, actualFilm, "Фильмы не совпадают");
    }

    /**
     * Пользователь ставит фильму лайк, возвращаем популярные фильмы
     */
    @Test
    void isAddLikeFilms() {
        filmDao.addLikeFilms(String.valueOf(film1.getId()), String.valueOf(user1.getId()));
        filmDao.addLikeFilms(String.valueOf(film2.getId()), String.valueOf(user1.getId()));
        filmDao.addLikeFilms(String.valueOf(film2.getId()), String.valueOf(user2.getId()));
        List<Film> expected = new ArrayList<>();
        expected.add(film2);
        expected.add(film1);

        List<Film> popularFilms = filmDao.getPopularFilms("2");

        assertEquals(expected, popularFilms, "Списки не совпадают");
    }

    /**
     * тест на удаление фильма по id
     */
    @Test
    public void isDeleteFilmById() {
        filmDao.deleteFilmById(String.valueOf(film1.getId()));

        assertThrows(NotFoundFilmException.class, () -> {
            filmDao.findFilmById("1");
        });
    }

    /**
     * Тест на удаление фильмов из списка
     */
    @Test
    void isClearFilms() {
        List<Film> expectedList = new ArrayList<>();

        filmDao.clearFilms();
        List<Film> actualListFilms = filmDao.getFilms();

        assertEquals(expectedList, actualListFilms, "Список не пуст");
    }
}