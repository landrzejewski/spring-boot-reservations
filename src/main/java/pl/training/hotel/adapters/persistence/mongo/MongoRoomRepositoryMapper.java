package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.stereotype.Component;
import pl.training.hotel.domain.*;

import java.time.ZoneOffset;
import java.util.Currency;
import java.util.UUID;

@Component
public class MongoRoomRepositoryMapper {

    public RoomDocument toDocument(final Room room) {
        var pricePerNight = room.getPricePerNight();
        return new RoomDocument(
                room.getId().value().toString(),
                room.getNumber().value(),
                room.getType().name(),
                pricePerNight.amount(),
                pricePerNight.currency().getCurrencyCode(),
                room.getReservations().stream().map(this::toDocument).toList()
        );
    }

    private ReservationDocument toDocument(final Reservation reservation) {
        return new ReservationDocument(
                reservation.id().value().toString(),
                reservation.timestamp().toInstant(),
                reservation.period().checkIn(),
                reservation.period().checkOut(),
                reservation.guests(),
                reservation.value().amount()
        );
    }

    public Room toDomain(final RoomDocument roomDocument) {
        var pricePerNight = new Money(roomDocument.pricePerNight(), Currency.getInstance(roomDocument.currencyCode()));
        var room = new Room(
                new RoomId(UUID.fromString(roomDocument.id())),
                new RoomNumber(roomDocument.number()),
                RoomType.valueOf(roomDocument.type()),
                pricePerNight
        );
        roomDocument.reservations().stream()
                .map(reservationDocument -> toDomain(reservationDocument, pricePerNight))
                .forEach(room::registerReservation);
        return room;
    }

    private Reservation toDomain(final ReservationDocument reservationDocument, final Money pricePerNight) {
        return new Reservation(
                new ReservationId(UUID.fromString(reservationDocument.id())),
                reservationDocument.placedAt().atZone(ZoneOffset.UTC),
                new StayPeriod(reservationDocument.checkIn(), reservationDocument.checkOut()),
                reservationDocument.guests(),
                new Money(reservationDocument.price(), pricePerNight.currency())
        );
    }

}
