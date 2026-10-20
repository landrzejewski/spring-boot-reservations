package pl.training;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.training.common.PageSpec;
import pl.training.common.aop.ThrottlingException;
import pl.training.common.audit.AuditLog;
import pl.training.hotel.adapters.InMemoryReservationEventPublisher;
import pl.training.hotel.adapters.StayPolicyViolationException;
import pl.training.hotel.application.AddReservationUseCase;
import pl.training.hotel.application.AddRoomUseCase;
import pl.training.hotel.application.GetRoomUseCase;
import pl.training.hotel.application.GetRoomsUseCase;
import pl.training.hotel.domain.Money;
import pl.training.hotel.domain.RoomNotAvailableException;
import pl.training.hotel.domain.StayPeriod;

import java.time.LocalDate;
import java.util.Currency;
import java.util.logging.Logger;

import static pl.training.hotel.domain.RoomType.DOUBLE;
import static pl.training.hotel.domain.RoomType.SINGLE;
import static pl.training.hotel.domain.RoomType.SUITE;

@Order(1)
@Component
public class HotelDemo implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getLogger(HotelDemo.class.getName());
    private static final Currency CURRENCY = Currency.getInstance("PLN");

    private final AddRoomUseCase addRoomUseCase;
    private final AddReservationUseCase addReservationUseCase;
    private final GetRoomsUseCase getRoomsUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final ObjectProvider<InMemoryReservationEventPublisher> reservationHistory;
    private final AuditLog auditLog;

    public HotelDemo(AddRoomUseCase addRoomUseCase, AddReservationUseCase addReservationUseCase,
                     GetRoomsUseCase getRoomsUseCase, GetRoomUseCase getRoomUseCase,
                     ObjectProvider<InMemoryReservationEventPublisher> reservationHistory, AuditLog auditLog) {
        this.addRoomUseCase = addRoomUseCase;
        this.addReservationUseCase = addReservationUseCase;
        this.getRoomsUseCase = getRoomsUseCase;
        this.getRoomUseCase = getRoomUseCase;
        this.reservationHistory = reservationHistory;
        this.auditLog = auditLog;
    }

    @Override
    public void run(String... args) {
        var roomNumber = addRoomUseCase.handle(DOUBLE, new Money(300.0, CURRENCY)).getNumber();
        addRoomUseCase.handle(DOUBLE, new Money(350.0, CURRENCY));
        addRoomUseCase.handle(SINGLE, new Money(200.0, CURRENCY));
        addRoomUseCase.handle(SUITE, new Money(800.0, CURRENCY));

        var today = LocalDate.now();
        addReservationUseCase.handle(roomNumber, new StayPeriod(today.plusDays(1), today.plusDays(4)), 2);
        addReservationUseCase.handle(roomNumber, new StayPeriod(today.plusDays(4), today.plusDays(6)), 1);
        try {
            addReservationUseCase.handle(roomNumber, new StayPeriod(today.plusDays(3), today.plusDays(5)), 2);
        } catch (RoomNotAvailableException exception) {
            LOGGER.info("Room %s is not available in the requested period".formatted(roomNumber.value()));
        }
        try {
            addReservationUseCase.handle(roomNumber, new StayPeriod(today.plusDays(10), today.plusDays(40)), 2);
        } catch (StayPolicyViolationException exception) {
            LOGGER.info(exception.getMessage());
        }

        getRoomsUseCase.handle(new PageSpec(0, 10))
                .content()
                .forEach(room -> LOGGER.info(room.toString()));
        try {
            for (int attempt = 0; attempt < 3; attempt++) {
                getRoomsUseCase.handle(new PageSpec(0, 10));
            }
        } catch (ThrottlingException exception) {
            LOGGER.info(exception.getMessage());
        }

        getRoomUseCase.handle(roomNumber)
                .getReservations()
                .forEach(reservation -> LOGGER.info(reservation.toString()));

        reservationHistory.ifAvailable(history -> LOGGER.info("Reservation history: " + history.getPublishedEvents()));
        LOGGER.info("Audit log contains %d entries".formatted(auditLog.getEntries().size()));
    }

}
