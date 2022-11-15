package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Genre implements Comparable<Genre> {
    @NonNull
    private int id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(this.id, o.id);
    }
}
