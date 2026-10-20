package pl.training.hotel.domain;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public record StayPeriod(LocalDate checkIn, LocalDate checkOut) {

    public StayPeriod {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    public int nights() {
        return (int) DAYS.between(checkIn, checkOut);
    }

    public boolean overlaps(final StayPeriod stayPeriod) {
        return checkIn.isBefore(stayPeriod.checkOut) && stayPeriod.checkIn.isBefore(checkOut);
    }

}
