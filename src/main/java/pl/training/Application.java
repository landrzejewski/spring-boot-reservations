package pl.training;

import pl.training.common.PageSpec;
import pl.training.hotel.HotelConfiguration;
import pl.training.hotel.domain.Money;
import pl.training.hotel.domain.RoomNotAvailableException;
import pl.training.hotel.domain.StayPeriod;

import java.time.LocalDate;
import java.util.Currency;
import java.util.logging.Logger;

import static pl.training.hotel.domain.RoomType.DOUBLE;
import static pl.training.hotel.domain.RoomType.SINGLE;
import static pl.training.hotel.domain.RoomType.SUITE;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    private static final Currency CURRENCY = Currency.getInstance("PLN");

    static void main() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

        var hotelConfiguration = new HotelConfiguration();
        var addRoomUseCase = hotelConfiguration.addRoomUseCase();
        var addReservationUseCase = hotelConfiguration.addReservationUseCase();
        var getRoomsUseCase = hotelConfiguration.getRoomsUseCase();
        var getRoomUseCase = hotelConfiguration.getRoomUseCase();

        var roomNumber = addRoomUseCase.handle(DOUBLE, new Money(300.0, CURRENCY)).getNumber();
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

        getRoomsUseCase.handle(new PageSpec(0, 10))
                .content()
                .forEach(room -> LOGGER.info(room.toString()));

        getRoomUseCase.handle(roomNumber)
                .getReservations()
                .forEach(reservation -> LOGGER.info(reservation.toString()));
    }

}
