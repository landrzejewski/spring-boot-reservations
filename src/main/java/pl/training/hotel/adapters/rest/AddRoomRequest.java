package pl.training.hotel.adapters.rest;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pl.training.hotel.domain.Money;
import pl.training.hotel.domain.RoomType;

import java.math.BigDecimal;
import java.util.Currency;

record AddRoomRequest(@NotNull RoomType type,
                      @NotNull @DecimalMin("50.00") @DecimalMax("10000.00") BigDecimal pricePerNight,
                      @NotNull @Pattern(regexp = "[A-Z]{3}") String currencyCode) {

    Money price() {
        return new Money(pricePerNight, Currency.getInstance(currencyCode));
    }

}
