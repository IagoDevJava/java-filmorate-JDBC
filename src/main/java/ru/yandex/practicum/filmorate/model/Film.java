package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film implements Comparable<Film> {
    private int id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private String rate;
    private Mpa mpa;
    private TreeSet<Genre> genres;

    @Override
    public int compareTo(Film o) {
        return Comparator.comparing(Film::getGenres, Comparator.comparingInt(Set::size)).compare(this, o);
    }
}
