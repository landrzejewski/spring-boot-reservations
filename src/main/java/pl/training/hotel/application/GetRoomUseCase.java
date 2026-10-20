package pl.training.hotel.application;

import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

public class GetRoomUseCase {

    private final RoomRepository roomRepository;

    public GetRoomUseCase(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room handle(final RoomNumber roomNumber) {
        return roomRepository.findByNumber(roomNumber)
                .orElseThrow(RoomNotFoundException::new);
    }

}
