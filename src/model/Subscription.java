package model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// mówimy Jacksonowi: dodaj do JSON-a pole o nazwie "type", które posłuży do identyfikacji
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
// mapujemy wartości tego pola na konkretne klasy
@JsonSubTypes({
        @JsonSubTypes.Type(value = DailySub.class, name = "DAILY"),
        @JsonSubTypes.Type(value = WeeklySub.class, name = "WEEKLY"),
        @JsonSubTypes.Type(value = MonthlySub.class, name = "MONTHLY"),
        @JsonSubTypes.Type(value = YearlySub.class, name = "YEARLY")
})

public abstract class Subscription {
    protected String name;
    protected double cost;
    protected Currency currency;
    protected Category category;
    protected LocalDate startDate;

    public Subscription(){}; //pusty konstruktor aby Jackson zadzialal (najpierw tworzy pusty obiekt a pozniej go wypełnia)

    public Subscription(String name, double cost, Currency currency, Category category, LocalDate startDate)
    {
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.category = category;
        this.startDate = startDate;
    }

    public abstract String getType();

    @JsonIgnore
    public abstract double getAnnualCost();

    @JsonIgnore
    public abstract LocalDate getNextPaymentDate();

    @Override
    public String toString()
    {
        return String.format("%-10s | Cost: %.2f %s | Yearly: %.2f %s | Category: %-10s | Next payment: %-10s",
                name, cost, currency, getAnnualCost(), currency, category, getNextPaymentDate().toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
