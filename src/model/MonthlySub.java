package model;

import logic.*;
import java.time.LocalDate;

public class MonthlySub extends Subscription {

    public MonthlySub(){super();}

    public MonthlySub(String name, double cost, Currency currency, Category category, LocalDate startDay) {
        super(name, cost, currency, category, startDay);
    }

    @Override
    public double getAnnualCost()
    {
        return cost*12;
    }

    @Override
    public LocalDate getNextPaymentDate()
    {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = startDate;

        while (!nextDate.isAfter(today)) {
            nextDate = nextDate.plusMonths(1);
        }
        return nextDate;
    }

    @Override
    public String getType()
    {
        return "MONTHLY";
    }

}
