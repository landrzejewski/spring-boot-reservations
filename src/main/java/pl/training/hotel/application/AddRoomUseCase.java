package pl.training.hotel.application;

import org.springframework.transaction.annotation.Transactional;
import pl.training.common.audit.Audited;
import pl.training.hotel.domain.Money;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomId;
import pl.training.hotel.domain.RoomNumber;
import pl.training.hotel.domain.RoomType;

import static pl.training.hotel.domain.RoomType.SUITE;

@Transactional
public class AddRoomUseCase {

    private final RoomNumberGenerator standardRoomNumberGenerator;
    private final RoomNumberGenerator suiteRoomNumberGenerator;
    private final RoomRepository roomRepository;

    public AddRoomUseCase(final RoomNumberGenerator standardRoomNumberGenerator,
                          final RoomNumberGenerator suiteRoomNumberGenerator,
                          final RoomRepository roomRepository) {
        this.standardRoomNumberGenerator = standardRoomNumberGenerator;
        this.suiteRoomNumberGenerator = suiteRoomNumberGenerator;
        this.roomRepository = roomRepository;
    }

    @Audited("ADD_ROOM")
    public Room handle(final RoomType type, final Money pricePerNight) {
        var room = createRoom(type, pricePerNight);
        return roomRepository.save(room);
    }

    private Room createRoom(final RoomType type, final Money pricePerNight) {
        return new Room(new RoomId(), nextRoomNumber(type), type, pricePerNight);
    }

    private RoomNumber nextRoomNumber(final RoomType type) {
        var generator = type == SUITE ? suiteRoomNumberGenerator : standardRoomNumberGenerator;
        return generator.getNext();
    }

}
