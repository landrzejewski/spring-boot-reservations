package pl.training.hotel.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.training.hotel.application.RoomNumberGenerator;
import pl.training.hotel.domain.RoomNumber;

@Component("suiteRoomNumberGenerator")
public class SequentialRoomNumberGenerator implements RoomNumberGenerator {

    private static final int MIN_START_NUMBER = 100;

    private int nextNumber;

    @Autowired
    public SequentialRoomNumberGenerator(final RoomNumberProperties properties) {
        this(properties.suiteStartNumber());
    }

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
