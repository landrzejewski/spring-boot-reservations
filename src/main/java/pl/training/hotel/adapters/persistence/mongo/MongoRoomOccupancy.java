package pl.training.hotel.adapters.persistence.mongo;

public record MongoRoomOccupancy(String number, long reservations, long guests) {
}
