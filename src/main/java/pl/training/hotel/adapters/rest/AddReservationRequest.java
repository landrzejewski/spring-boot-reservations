package pl.training.hotel.adapters.rest;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.training.common.validation.DateRange;
import pl.training.common.validation.ValidDateRange;
import pl.training.hotel.domain.StayPeriod;

import java.time.LocalDate;

@ValidDateRange
record AddReservationRequest(@NotNull @FutureOrPresent LocalDate checkIn,
                             @NotNull LocalDate checkOut,
                             @Min(1) int guests) implements DateRange {

    StayPeriod stayPeriod() {
        return new StayPeriod(checkIn, checkOut);
    }

    @Override
    public LocalDate startDate() {
        return checkIn;
    }

    @Override
    public LocalDate endDate() {
        return checkOut;
    }

}
