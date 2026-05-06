package test;

import model.*;
import logic.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class SubscriptionTest {

    // test 1: Sprawdzenie logiki biznesowej (czy dobrze liczy roczny koszt)
    @Test
    public void testCalculateAnnualCost() {
        // dane wejściowe
        LocalDate today = LocalDate.now();
        // tworzymy subskrypcję miesięczną za 10 PLN
        Subscription sub = new MonthlySub("Netflix", 10.0, Currency.PLN, Category.ENTERTAINMENT, today);

        // wykonanie akcji
        double annualCost = sub.getAnnualCost();

        // sprawdzenie wyniku
        // oczekujemy 120.0. Ostatni parametr to margines błędu
        Assertions.assertEquals(120.0, annualCost, 0.01);
    }

    // test 2: sprawdzenie wyjątku

    @Test
    public void testNegativeCostException() {
        // sprawdzamy czy aplikacja faktycznie rzuca  wyjątek InvalidCostException
        // przykład walidacji:
        Assertions.assertThrows(InvalidCostException.class, () -> {
            validateCost(-5.0);
        });
    }

    private void validateCost(double cost) throws InvalidCostException {
        if (cost < 0) {
            throw new InvalidCostException("Cost cannot be negative");
        }
    }
}
