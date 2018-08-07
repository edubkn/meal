package br.com.meal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.meal.exception.InputException;
import br.com.meal.model.DishType;
import br.com.meal.model.Dish;
import br.com.meal.model.DayPeriod;

import static java.util.stream.Collectors.groupingBy;

public class Selection {

    private static final List<Dish> menu;

    static {
        menu = Arrays.asList(
                new Dish(DayPeriod.MORNING, DishType.ENTREE, "eggs", false),
                new Dish(DayPeriod.MORNING, DishType.SIDE, "Toast", false),
                new Dish(DayPeriod.MORNING, DishType.DRINK, "coffee", true),
                new Dish(DayPeriod.NIGHT, DishType.ENTREE, "steak", false),
                new Dish(DayPeriod.NIGHT, DishType.SIDE, "potato", true),
                new Dish(DayPeriod.NIGHT, DishType.DRINK, "wine", false),
                new Dish(DayPeriod.NIGHT, DishType.DESSERT, "cake", false)
        );
    }

    private final PrintStream printer;

    public Selection(PrintStream printer) {
        this.printer = printer;
    }

    public void processSelection(String input) throws InputException {
        List<String> selections = new ArrayList<>(Arrays.asList(input.split(",")));
        String type = selections.remove(0);
        List<Optional<Dish>> order = buildOrder(selections, validatePeriod(type));

        if (!order.contains(Optional.empty()) && !containsUnrepeatable(order)) {
            order.sort(Comparator.comparing(o -> o.get().getIndex().ordinal()));
        }

        printOrder(selections, type, order);
    }

    /**
     * This method will prevent the list to be sorted when it contains an unrepeatable (invalid) dish
     * @param order the list of dishes ordered
     * @return true if the list contains a dish that can't be repeated
     */
    private boolean containsUnrepeatable(List<Optional<Dish>> order) {
        return order.stream()
                .map(Optional::get)
                .filter(d -> !d.isRepeatable())
                .collect(groupingBy(Dish::getIndex, Collectors.counting()))
                .values().stream().anyMatch(count -> count > 1);
    }

    /**
     * @param type a user selected period
     * @return a {@link DayPeriod} of the inputted type
     * @throws InputException if there is no such period
     */
    private DayPeriod validatePeriod(String type) throws InputException {
        return DayPeriod.valueOfIgnoreCase(type)
                .orElseThrow(() -> new InputException("Invalid period: " + type));
    }

    /**
     * @param options the dish numbers selected by the user
     * @param period the period of the day selected by the user
     * @return a list of dishes
     */
    private List<Optional<Dish>> buildOrder(List<String> options, DayPeriod period) {
        return options.stream()
                .map(s -> findInMenu(s, period))
                .collect(Collectors.toList());
    }

    /**
     * Searches the menu for a dish.
     * @param selection user dish selection
     * @param period period of the day
     * @return the
     */
    private Optional<Dish> findInMenu(String selection, DayPeriod period) {
        return menu.stream()
                .filter(m -> m.getType() == period
                        && Integer.valueOf(selection).equals(m.getIndex().ordinal() + 1))
                .findAny();
    }

    private void printOrder(List<String> selections, String type, List<Optional<Dish>> order) throws InputException {
        ListIterator<Optional<Dish>> iterator = order.listIterator();
        List<Dish> printed = new ArrayList<>();
        while (iterator.hasNext()) {
            Dish dish = iterator.next()
                    .orElseThrow(() -> new InputException("Invalid dish: " + type + "," + selections.get(iterator.previousIndex())));

            if (printed.contains(dish) && !dish.isRepeatable()) {
                throw new InputException("You can only order 1 of this dish type: " + selections.get(iterator.previousIndex()));
            }
            printer.print(dish.getName());
            printed.add(dish);

            if (iterator.hasNext()) {
                printer.print(',');
            }
        }
    }

}
