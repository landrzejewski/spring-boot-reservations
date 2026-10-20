package pl.training.hotel.application;

import pl.training.hotel.domain.RoomNumber;

public interface RoomNumberGenerator {

    RoomNumber getNext();

}
