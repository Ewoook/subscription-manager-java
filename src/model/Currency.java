package model;

public enum Currency {
    PLN("zł", 1.0),
    USD("$", 3.95),
    EUR("€", 4.30);

    private final String symbol;
    private final double conversionFactor;
    Currency(String symbol, double conversionFactor)
    {
        this.symbol = symbol;
        this.conversionFactor = conversionFactor;
    }

    public String formatAmount(double amount)
    {
        return String.format("%.2f %s", amount, symbol);
    }

    @Override
    public String toString()
    {
        return symbol;
    }
}
