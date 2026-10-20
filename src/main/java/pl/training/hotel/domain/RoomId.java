package pl.training.hotel.domain;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record RoomId(UUID value) {

    public RoomId() {
        this(randomUUID());
    }

}
