package model;

import java.time.LocalDate;

public class DailySub extends Subscription {

    public DailySub(){super();}

    public DailySub(String name, double cost, Currency currency, Category category, LocalDate startDate) {
        super(name, cost, currency, category, startDate);
    }

    @Override
    public double getAnnualCost()
    {
        return cost*365;
    }

    @Override
    public LocalDate getNextPaymentDate()
    {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = startDate;

        while (!nextDate.isAfter(today)) {
            nextDate = nextDate.plusDays(1);
        }
        return nextDate;
    }

    @Override
    public String getType()
    {
        return "DAILY";
    }

}
