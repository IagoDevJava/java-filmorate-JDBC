package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    /**
     * возвращает список MPA.
     */
    public List<Mpa> getMpaService() {
        return mpaDao.getMpa();
    }

    /**
     * возвращает MPA по id.
     */
    public Optional<Mpa> getMpaFromFilmService(String idFilm) {
        return mpaDao.getMpaFromFilm(idFilm);
    }
}
