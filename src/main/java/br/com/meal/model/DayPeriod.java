package br.com.meal.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum DayPeriod {

    MORNING,
    NIGHT;

    public static Optional<DayPeriod> valueOfIgnoreCase(String value) {
        return Stream.of(DayPeriod.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findAny();
    }
}
