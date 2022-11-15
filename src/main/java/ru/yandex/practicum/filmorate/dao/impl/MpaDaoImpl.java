package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.MpaValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDaoImpl implements MpaDao {
    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * возвращает список MPA.
     */
    @Override
    public List<Mpa> getMpa() {
        List<Mpa> mpas = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA");
        while (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"))
                    .build();
            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
            mpas.add(mpa);
        }
        log.info("Вернули список MPA");
        return mpas;
    }

    /**
     * возвращает MPA по id.
     */
    @Override
    public Optional<Mpa> getMpaFromFilm(String idMpa) {
        MpaValidator.isValidIdMpa(Integer.parseInt(idMpa));
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "SELECT ID, NAME " +
                        "FROM MPA " +
                        "WHERE ID=?", idMpa);
        if (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"))
                    .build();
            log.info("Найден MPA: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("MPA с идентификатором {} не найден.", idMpa);
            throw new NotFoundMpaException("Такого MPA нет в базе.");
        }
    }
}
