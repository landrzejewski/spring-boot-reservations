package pl.training.hotel.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.training.hotel.application.RoomNumberGenerator;
import pl.training.hotel.domain.RoomNumber;

@Primary
@Component
public class FloorRoomNumberGenerator implements RoomNumberGenerator {

    private static final int MAX_ROOMS_PER_FLOOR = 99;

    private final int roomsPerFloor;

    private int counter;

    @Autowired
    public FloorRoomNumberGenerator(final RoomNumberProperties properties) {
        this(properties.roomsPerFloor());
    }

    public FloorRoomNumberGenerator(final int roomsPerFloor) {
        if (roomsPerFloor < 1 || roomsPerFloor > MAX_ROOMS_PER_FLOOR) {
            throw new IllegalArgumentException("Rooms per floor must be between 1 and " + MAX_ROOMS_PER_FLOOR);
        }
        this.roomsPerFloor = roomsPerFloor;
    }

    @Override
    public synchronized RoomNumber getNext() {
        var floor = counter / roomsPerFloor + 1;
        var room = counter % roomsPerFloor + 1;
        counter++;
        return new RoomNumber("%d%02d".formatted(floor, room));
    }

}
