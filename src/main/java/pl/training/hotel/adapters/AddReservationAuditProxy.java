package pl.training.hotel.adapters;

import pl.training.hotel.application.AddReservationUseCase;
import pl.training.hotel.application.DateTimeProvider;
import pl.training.hotel.application.ReservationEventPublisher;
import pl.training.hotel.application.RoomRepository;
import pl.training.hotel.domain.ReservationId;
import pl.training.hotel.domain.RoomNumber;
import pl.training.hotel.domain.StayPeriod;

import java.util.logging.Logger;

public final class AddReservationAuditProxy extends AddReservationUseCase {

    private static final Logger LOGGER = Logger.getLogger(AddReservationAuditProxy.class.getName());
    private static final String ACTION = "ADD_RESERVATION";

    public AddReservationAuditProxy(final DateTimeProvider dateTimeProvider,
                                    final ReservationEventPublisher reservationEventPublisher,
                                    final RoomRepository roomRepository) {
        super(dateTimeProvider, reservationEventPublisher, roomRepository);
    }

    @Override
    public ReservationId handle(final RoomNumber roomNumber, final StayPeriod stayPeriod, final int guests) {
        try {
            var reservationId = super.handle(roomNumber, stayPeriod, guests);
            LOGGER.info("AUDIT %s room=%s result=SUCCESS reservation=%s"
                    .formatted(ACTION, roomNumber.value(), reservationId.value()));
            return reservationId;
        } catch (RuntimeException runtimeException) {
            LOGGER.info("AUDIT %s room=%s result=FAILURE reason=%s"
                    .formatted(ACTION, roomNumber.value(), runtimeException.getClass().getSimpleName()));
            throw runtimeException;
        }
    }

}
