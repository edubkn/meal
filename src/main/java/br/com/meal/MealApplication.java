package br.com.meal;

import java.util.Scanner;

import br.com.meal.exception.InputException;

public class MealApplication {

    private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws InputException {
        System.out.println("Make your selection");
        String input = scanner.nextLine();

        Selection selection = new Selection(System.out);
        selection.processSelection(input);
	}

}
