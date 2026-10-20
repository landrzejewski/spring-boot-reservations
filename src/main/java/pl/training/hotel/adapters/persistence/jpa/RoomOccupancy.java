package pl.training.hotel.adapters.persistence.jpa;

public record RoomOccupancy(String number, Long reservations, Long guests) {
}
