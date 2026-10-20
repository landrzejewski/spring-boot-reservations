package pl.training.hotel.adapters.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.domain.*;

import java.util.Currency;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaRoomRepositoryMapper {

    public void update(final RoomEntity roomEntity, final Room room) {
        roomEntity.setId(room.getId().value().toString());
        roomEntity.setNumber(room.getNumber().value());
        roomEntity.setType(room.getType());
        roomEntity.setPricePerNight(toEntity(room.getPricePerNight()));
        roomEntity.setRevenue(toEntity(room.getRevenue()));
        var storedReservationIds = roomEntity.getReservations().stream()
                .map(ReservationEntity::getId)
                .collect(Collectors.toSet());
        room.getReservations().stream()
                .filter(reservation -> !storedReservationIds.contains(reservation.id().value().toString()))
                .map(this::toEntity)
                .forEach(roomEntity::addReservation);
    }

    private ReservationEntity toEntity(final Reservation reservation) {
        var reservationEntity = new ReservationEntity();
        reservationEntity.setId(reservation.id().value().toString());
        reservationEntity.setPlacedAt(reservation.timestamp());
        reservationEntity.setCheckIn(reservation.period().checkIn());
        reservationEntity.setCheckOut(reservation.period().checkOut());
        reservationEntity.setGuests(reservation.guests());
        reservationEntity.setPrice(toEntity(reservation.value()));
        return reservationEntity;
    }

    private MoneyEmbeddable toEntity(final Money money) {
        return new MoneyEmbeddable(money.amount(), money.currency().getCurrencyCode());
    }

    public PageRequest toEntity(final PageSpec pageSpec) {
        return PageRequest.of(pageSpec.index(), pageSpec.size(), Sort.by("number"));
    }

    public Room toDomain(final RoomEntity roomEntity) {
        var room = new Room(
                new RoomId(UUID.fromString(roomEntity.getId())),
                new RoomNumber(roomEntity.getNumber()),
                roomEntity.getType(),
                toDomain(roomEntity.getPricePerNight())
        );
        roomEntity.getReservations().stream()
                .map(this::toDomain)
                .forEach(room::registerReservation);
        return room;
    }

    private Reservation toDomain(final ReservationEntity reservationEntity) {
        return new Reservation(
                new ReservationId(UUID.fromString(reservationEntity.getId())),
                reservationEntity.getPlacedAt(),
                new StayPeriod(reservationEntity.getCheckIn(), reservationEntity.getCheckOut()),
                reservationEntity.getGuests(),
                toDomain(reservationEntity.getPrice())
        );
    }

    private Money toDomain(final MoneyEmbeddable money) {
        return new Money(money.getAmount(), Currency.getInstance(money.getCurrencyCode()));
    }

    public ResultPage<Room> toDomain(final Page<RoomEntity> page) {
        return new ResultPage<>(
                page.stream().map(this::toDomain).toList(),
                new PageSpec(page.getNumber(), page.getSize()),
                page.getTotalPages()
        );
    }

}
