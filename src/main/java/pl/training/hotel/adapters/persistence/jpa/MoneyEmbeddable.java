package pl.training.hotel.adapters.persistence.jpa;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class MoneyEmbeddable {

    private BigDecimal amount;
    private String currencyCode;

    public MoneyEmbeddable() {
    }

    public MoneyEmbeddable(BigDecimal amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return amount + " " + currencyCode;
    }

}
