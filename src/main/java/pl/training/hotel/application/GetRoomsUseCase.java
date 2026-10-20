package pl.training.hotel.application;

import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.domain.Room;

public class GetRoomsUseCase {

    private final RoomRepository roomRepository;

    public GetRoomsUseCase(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResultPage<Room> handle(final PageSpec pageSpec) {
        return roomRepository.findAll(pageSpec);
    }

}
