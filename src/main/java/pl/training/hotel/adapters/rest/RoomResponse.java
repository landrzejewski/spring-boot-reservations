package pl.training.hotel.adapters.rest;

import pl.training.hotel.domain.Reservation;
import pl.training.hotel.domain.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

record RoomResponse(String number, String type, int capacity, BigDecimal pricePerNight, String currencyCode,
                    BigDecimal revenue, List<ReservationResponse> reservations) {

    static RoomResponse from(final Room room) {
        return new RoomResponse(
                room.getNumber().value(),
                room.getType().name(),
                room.getType().getCapacity(),
                room.getPricePerNight().amount(),
                room.getPricePerNight().currency().getCurrencyCode(),
                room.getRevenue().amount(),
                room.getReservations().stream().map(ReservationResponse::from).toList()
        );
    }

    record ReservationResponse(String id, LocalDate checkIn, LocalDate checkOut, int guests, BigDecimal price) {

        static ReservationResponse from(final Reservation reservation) {
            return new ReservationResponse(
                    reservation.id().value().toString(),
                    reservation.period().checkIn(),
                    reservation.period().checkOut(),
                    reservation.guests(),
                    reservation.value().amount()
            );
        }

    }

}
