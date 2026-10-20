package pl.training.hotel.application;

import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    ResultPage<Room> findAll(PageSpec pageSpec);

    Optional<Room> findByNumber(RoomNumber roomNumber);

}
