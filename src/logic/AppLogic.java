package logic;

import model.Currency;
import model.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLogic {
    private Map<Currency, Double> exchangeRates;
    private List <Subscription> subscriptions;
    private JsonManager jsonManager;

    public AppLogic()
    {
        exchangeRates = new HashMap<>();
        exchangeRates.put(Currency.PLN, 1.0);
        exchangeRates.put(Currency.USD, 3.95);
        exchangeRates.put(Currency.EUR, 4.30);

        this.jsonManager = new JsonManager();
        this.subscriptions = jsonManager.loadSubscriptions();
    }

    public void addSub(Subscription sub)
    {
            subscriptions.add(sub);
            jsonManager.saveSubscriptions(subscriptions);
    }

    public void removeSub(Subscription sub)
    {
        subscriptions.remove(sub);
        jsonManager.saveSubscriptions(subscriptions);
    }

    public double exchange(double amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }

        double fromRate = exchangeRates.get(from);
        double toRate = exchangeRates.get(to);

        return (amount * fromRate) / toRate;
    }

    public double getTotalAnnualCost()
    {
        double total = 0;
        for(Subscription sub : subscriptions)
        {
            total += exchange(sub.getAnnualCost(), sub.getCurrency(), Currency.PLN);

        }
        return total;
    }

    public void updateRate(Currency currency, Double newRate)
    {
        exchangeRates.put(currency, newRate);
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Map<Currency, Double> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<Currency, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}
