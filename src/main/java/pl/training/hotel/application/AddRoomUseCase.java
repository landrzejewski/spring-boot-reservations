package pl.training.hotel.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.training.hotel.domain.Money;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomId;
import pl.training.hotel.domain.RoomNumber;
import pl.training.hotel.domain.RoomType;

import static pl.training.hotel.domain.RoomType.SUITE;

@Service
public class AddRoomUseCase {

    private final RoomNumberGenerator standardRoomNumberGenerator;
    private final RoomNumberGenerator suiteRoomNumberGenerator;
    private final RoomRepository roomRepository;

    public AddRoomUseCase(final RoomNumberGenerator standardRoomNumberGenerator,
                          @Qualifier("suiteRoomNumberGenerator") final RoomNumberGenerator suiteRoomNumberGenerator,
                          final RoomRepository roomRepository) {
        this.standardRoomNumberGenerator = standardRoomNumberGenerator;
        this.suiteRoomNumberGenerator = suiteRoomNumberGenerator;
        this.roomRepository = roomRepository;
    }

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
