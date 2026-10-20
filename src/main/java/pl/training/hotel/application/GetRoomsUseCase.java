package pl.training.hotel.application;

import org.springframework.stereotype.Service;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.domain.Room;

@Service
public class GetRoomsUseCase {

    private final RoomRepository roomRepository;

    public GetRoomsUseCase(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResultPage<Room> handle(final PageSpec pageSpec) {
        return roomRepository.findAll(pageSpec);
    }

}
