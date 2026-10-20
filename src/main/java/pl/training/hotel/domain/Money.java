package pl.training.hotel.domain;

import java.math.BigDecimal;
import java.util.Currency;

import static java.math.BigDecimal.ZERO;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equal zero");
        }
    }

    public Money(final double amount, final Currency currency) {
        this(BigDecimal.valueOf(amount), currency);
    }

    public Money(final double amount, final String currencyCode) {
        this(amount, Currency.getInstance(currencyCode));
    }

    public Money add(final Money money) {
        checkCurrency(money);
        return new Money(amount.add(money.amount), currency);
    }

    public Money multiply(final int multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    private void checkCurrency(final Money money) {
        if (!currency.equals(money.currency)) {
            throw new IllegalArgumentException("Currency does not match");
        }
    }

}
