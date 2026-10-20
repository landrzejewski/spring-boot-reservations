package pl.training.hotel.adapters;

import pl.training.hotel.application.RoomNumberGenerator;
import pl.training.hotel.domain.RoomNumber;

public class SequentialRoomNumberGenerator implements RoomNumberGenerator {

    private static final int MIN_START_NUMBER = 100;

    private int nextNumber;

    public SequentialRoomNumberGenerator(final int startNumber) {
        if (startNumber < MIN_START_NUMBER) {
            throw new IllegalArgumentException("Start number must be greater than or equal to " + MIN_START_NUMBER);
        }
        this.nextNumber = startNumber;
    }

    @Override
    public synchronized RoomNumber getNext() {
        return new RoomNumber(String.valueOf(nextNumber++));
    }

}
