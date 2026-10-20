package pl.training.hotel.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record Reservation(ReservationId id, ZonedDateTime timestamp, StayPeriod period, int guests, Money value) {

    public Reservation {
        if (guests < 1) {
            throw new IllegalArgumentException("Number of guests must be greater than zero");
        }
    }

    public boolean overlaps(final Reservation reservation) {
        return period.overlaps(reservation.period);
    }

    public boolean startsBefore(final LocalDate date) {
        return period.checkIn()
                .isBefore(date);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id.value() +
                ", timestamp=" + timestamp +
                ", checkIn=" + period.checkIn() +
                ", checkOut=" + period.checkOut() +
                ", guests=" + guests +
                ", value=" + value +
                '}';
    }

}
