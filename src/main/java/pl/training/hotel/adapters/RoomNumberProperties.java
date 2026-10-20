package pl.training.hotel.adapters;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hotel.room-numbers")
public record RoomNumberProperties(int roomsPerFloor, int suiteStartNumber) {
}
