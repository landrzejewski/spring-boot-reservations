package pl.training.hotel.application;

import org.springframework.stereotype.Service;
import pl.training.hotel.domain.*;

import java.util.List;
import java.util.function.Consumer;

@Service
public class AddReservationUseCase {

    private final DateTimeProvider dateTimeProvider;
    private final List<ReservationEventPublisher> reservationEventPublishers;
    private final RoomRepository roomRepository;

    public AddReservationUseCase(final DateTimeProvider dateTimeProvider,
                                 final List<ReservationEventPublisher> reservationEventPublishers,
                                 final RoomRepository roomRepository) {
        this.dateTimeProvider = dateTimeProvider;
        this.reservationEventPublishers = reservationEventPublishers;
        this.roomRepository = roomRepository;
    }

    public ReservationId handle(final RoomNumber roomNumber, final StayPeriod stayPeriod, final int guests) {
        var room = findRoom(roomNumber);
        var reservation = createReservation(room, stayPeriod, guests);
        var roomEventListener = createRoomEventListener();
        addReservationToRoom(room, reservation, roomEventListener);
        return reservation.id();
    }

    private Room findRoom(final RoomNumber roomNumber) {
        return roomRepository.findByNumber(roomNumber)
                .orElseThrow(RoomNotFoundException::new);
    }

    private Reservation createReservation(final Room room, final StayPeriod stayPeriod, final int guests) {
        var value = room.calculatePrice(stayPeriod);
        return new Reservation(new ReservationId(), dateTimeProvider.getZonedDateTime(), stayPeriod, guests, value);
    }

    private Consumer<ReservationRegistered> createRoomEventListener() {
        return event -> {
            var roomNumber = event.roomNumber().value();
            var reservationId = event.reservation().id().value().toString();
            var guests = event.reservation().guests();
            var applicationEvent = new ReservationAdded(roomNumber, reservationId, guests);
            reservationEventPublishers.forEach(publisher -> publisher.publish(applicationEvent));
        };
    }

    private void addReservationToRoom(final Room room, final Reservation reservation,
                                      final Consumer<ReservationRegistered> roomEventListener) {
        room.addEventListener(roomEventListener);
        room.registerReservation(reservation);
        room.removeEventListener(roomEventListener);
        roomRepository.save(room);
    }

}
