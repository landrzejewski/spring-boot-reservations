package pl.training.hotel.adapters.rest;

import pl.training.hotel.domain.ReservationId;

record AddReservationResponse(String id) {

    static AddReservationResponse from(final ReservationId reservationId) {
        return new AddReservationResponse(reservationId.value().toString());
    }

}
