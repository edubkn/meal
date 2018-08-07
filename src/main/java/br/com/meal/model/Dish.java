package br.com.meal.model;

import java.util.Objects;

public class Dish {

    private final DayPeriod type;
    private final DishType index;
    private final String name;
    private final boolean repeatable;

    public Dish(DayPeriod type, DishType index, String name, boolean repeatable) {
        this.type = type;
        this.index = index;
        this.name = name;
        this.repeatable = repeatable;
    }

    public DayPeriod getType() {
        return type;
    }

    public DishType getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish meal = (Dish) o;
        return repeatable == meal.repeatable &&
                type == meal.type &&
                index == meal.index &&
                Objects.equals(name, meal.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, index, name, repeatable);
    }
}
