package pl.training.hotel.application;

import pl.training.hotel.domain.*;

import java.util.function.Consumer;

public class AddReservationUseCase {

    private final DateTimeProvider dateTimeProvider;
    private final ReservationEventPublisher reservationEventPublisher;
    private final RoomRepository roomRepository;

    public AddReservationUseCase(final DateTimeProvider dateTimeProvider,
                                 final ReservationEventPublisher reservationEventPublisher,
                                 final RoomRepository roomRepository) {
        this.dateTimeProvider = dateTimeProvider;
        this.reservationEventPublisher = reservationEventPublisher;
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
            reservationEventPublisher.publish(applicationEvent);
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
