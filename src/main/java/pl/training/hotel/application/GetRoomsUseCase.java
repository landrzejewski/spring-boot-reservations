package pl.training.hotel.application;

import org.springframework.transaction.annotation.Transactional;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.common.aop.Throttled;
import pl.training.hotel.domain.Room;

@Transactional(readOnly = true)
public class GetRoomsUseCase {

    private final RoomRepository roomRepository;

    public GetRoomsUseCase(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Throttled(permits = 3, periodInSeconds = 1)
    public ResultPage<Room> handle(final PageSpec pageSpec) {
        return roomRepository.findAll(pageSpec);
    }

}
