package pl.training.hotel;

import pl.training.hotel.adapters.*;
import pl.training.hotel.application.*;

public class HotelConfiguration {

    private final RoomRepository roomRepository = new HashMapRoomRepository();
    private final RoomNumberGenerator standardRoomNumberGenerator = new FloorRoomNumberGenerator(10);
    private final RoomNumberGenerator suiteRoomNumberGenerator = new SequentialRoomNumberGenerator(900);
    private final DateTimeProvider dateTimeProvider = new SystemDateTimeProvider();
    private final ReservationEventPublisher reservationEventPublisher = new ConsoleReservationEventPublisher();

    public AddRoomUseCase addRoomUseCase() {
        return new AddRoomUseCase(standardRoomNumberGenerator, suiteRoomNumberGenerator, roomRepository);
    }

    public GetRoomsUseCase getRoomsUseCase() {
        return new GetRoomsUseCase(roomRepository);
    }

    public GetRoomUseCase getRoomUseCase() {
        return new GetRoomUseCase(roomRepository);
    }

    public AddReservationUseCase addReservationUseCase() {
        return new AddReservationAuditProxy(dateTimeProvider, reservationEventPublisher, roomRepository);
    }

}
