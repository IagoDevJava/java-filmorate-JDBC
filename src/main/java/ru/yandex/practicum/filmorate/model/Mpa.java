package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Mpa {
    @NonNull
    private int id;
    @NonNull
    private String name;
}
