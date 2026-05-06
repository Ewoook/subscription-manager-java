package model;

import java.time.LocalDate;

public class WeeklySub extends Subscription {

    public WeeklySub(){super();}

    public WeeklySub(String name, double cost, Currency currency, Category category, LocalDate startDate) {
        super(name, cost, currency, category, startDate);
    }

    @Override
    public double getAnnualCost()
    {
        return cost*52;
    }

    @Override
    public LocalDate getNextPaymentDate()
    {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = startDate;

        while (!nextDate.isAfter(today)) {
            nextDate = nextDate.plusWeeks(1);
        }
        return nextDate;
    }

    @Override
    public String getType()
    {
        return "WEEKLY";
    }

}
