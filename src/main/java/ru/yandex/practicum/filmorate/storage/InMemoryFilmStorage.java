package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    /**
     * Хранение списка добавленных фильмов
     */
    private final Map<Integer, Film> films = new HashMap<>();
    private int idFilm = 0;

    /**
     * добавление фильма в список
     */
    @Override
    public Film addFilm(Film film) {
        FilmValidator.isValidFilms(film);
        int id = generateIdFilms();
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    /**
     * обновление фильма в списке
     */
    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            FilmValidator.isValidFilms(film);
            films.put(film.getId(), film);
        } else {
            throw new NotFoundFilmException("Такого фильма нет в базе.");
        }
        return film;
    }

    /**
     * Очистить список фильмов
     */
    @Override
    public void clearFilms() {
        if (!films.isEmpty()) {
            films.clear();
        }
    }

    /**
     * Удаление фильма по id
     */
    @Override
    public void deleteFilmById(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!films.containsKey(id)) {
            throw new NotFoundFilmException(String.format("Фильм № %d не найден", id));
        }
        films.remove(id);
    }

    /**
     * получение всех фильмов
     */
    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    /**
     * получение фильма по id
     */
    @Override
    public Film findFilmById(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!films.containsKey(id)) {
            throw new NotFoundFilmException(String.format("Фильм № %d не найден", id));
        }
        return getFilms().stream().filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundFilmException(String.format("Film %d not found", id)));
    }

    /**
     * создание уникального id фильма
     */
    private int generateIdFilms() {
        return ++idFilm;
    }
}
