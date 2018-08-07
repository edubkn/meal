package br.com.meal;

import java.io.PrintStream;

import br.com.meal.exception.InputException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class MealApplicationTests {

    private Selection selection;
    @Spy
    private PrintStream printer = System.out;

    @Before
    public void setUp() {
        selection = new Selection(printer);
    }

    /*
    Dish Type   morning night
    1 (entrée)  eggs    steak
    2 (side)    Toast   potato
    3 (drink)   coffee  wine
    4 (dessert) N/A     cake
     */

    @Test
    public void shouldPrintSelectionUnordered() throws InputException {
        selection.processSelection("night,3,1");

        InOrder inOrder = inOrder(printer);
        inOrder.verify(printer).print(eq("steak"));
        inOrder.verify(printer).print(eq("wine"));
    }

    @Test
    public void shouldPrintSelectionOrderedDoubleCoffee() throws InputException {
        selection.processSelection("morning,1,2,3,3");

        InOrder inOrder = inOrder(printer);
        inOrder.verify(printer).print(eq("eggs"));
        inOrder.verify(printer).print(eq("Toast"));
        inOrder.verify(printer).print(eq("coffee"));
    }

    @Test
    public void shouldPrintSelectionUnorderedTriplePotato() throws InputException {
        selection.processSelection("night,2,2,4,3,1,2");

        InOrder inOrder = inOrder(printer);
        inOrder.verify(printer).print(eq("steak"));
        inOrder.verify(printer).print(eq("potato"));
        inOrder.verify(printer).print(eq("wine"));
        inOrder.verify(printer).print(eq("cake"));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldPrintSelectionAndError() throws InputException {
        thrown.expect(InputException.class);
        thrown.expectMessage("Invalid dish");

        selection.processSelection("night,3,1,2,5,4");
    }

    @Test
    public void shouldPrintSelectionAndNotRepeatable() throws InputException {
        thrown.expect(InputException.class);
        thrown.expectMessage("You can only order 1 of this dish type");

        selection.processSelection("morning,2,1,3,3,1");
    }


}
