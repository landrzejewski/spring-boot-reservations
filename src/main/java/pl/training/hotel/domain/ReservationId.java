package pl.training.hotel.domain;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record ReservationId(UUID value) {

    public ReservationId() {
        this(randomUUID());
    }

}
