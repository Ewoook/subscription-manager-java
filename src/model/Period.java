package model;

public enum Period {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    public String symbol;

    Period(String symbol)
    {
        this.symbol = symbol;
    }

    @Override
    public String toString()
    {
        return symbol;
    }
}
