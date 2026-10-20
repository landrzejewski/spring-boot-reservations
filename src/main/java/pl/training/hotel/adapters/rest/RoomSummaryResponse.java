package pl.training.hotel.adapters.rest;

import pl.training.hotel.domain.Room;

import java.math.BigDecimal;

record RoomSummaryResponse(String number, String type, BigDecimal pricePerNight, String currencyCode) {

    static RoomSummaryResponse from(final Room room) {
        return new RoomSummaryResponse(
                room.getNumber().value(),
                room.getType().name(),
                room.getPricePerNight().amount(),
                room.getPricePerNight().currency().getCurrencyCode()
        );
    }

}
