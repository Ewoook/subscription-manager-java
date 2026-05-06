package model;

import java.time.LocalDate;

public class YearlySub extends Subscription {

    public YearlySub(){super();}

    public YearlySub(String name, double cost, Currency currency, Category category, LocalDate startDate) {
        super(name, cost, currency, category, startDate);
    }

    @Override
    public double getAnnualCost()
    {
        return cost;
    }

    @Override
    public LocalDate getNextPaymentDate()
    {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = startDate;

        while (!nextDate.isAfter(today)) {
            nextDate = nextDate.plusYears(1);
        }
        return nextDate;
    }

    @Override
    public String getType()
    {
        return "YEARLY";
    }

}
