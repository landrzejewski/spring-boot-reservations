package pl.training.hotel.adapters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.training.hotel.application.ReservationAdded;
import pl.training.hotel.application.ReservationEventPublisher;

import java.util.logging.Logger;

@Order(1)
@Component
public final class ConsoleReservationEventPublisher implements ReservationEventPublisher {

    private static final Logger LOGGER = Logger.getLogger(ConsoleReservationEventPublisher.class.getName());

    @Override
    public void publish(final ReservationAdded reservationAdded) {
        LOGGER.info("Event: reservation %s for %d guest(s) added to room %s"
                .formatted(reservationAdded.reservationId(), reservationAdded.guests(), reservationAdded.roomNumber()));
    }

}
