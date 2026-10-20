package pl.training.hotel.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.math.BigDecimal.ZERO;

public final class Room {

    private final RoomId id;
    private final RoomNumber number;
    private final RoomType type;
    private final Money pricePerNight;
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Consumer<ReservationRegistered>> eventListeners = new ArrayList<>();

    private Money revenue;

    public Room(final RoomId id, final RoomNumber number, final RoomType type, final Money pricePerNight) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.revenue = new Money(ZERO, pricePerNight.currency());
    }

    public void registerReservation(final Reservation reservation) {
        validate(reservation);
        commit(reservation);
        publishEvents(reservation);
    }

    public Money calculatePrice(final StayPeriod stayPeriod) {
        return pricePerNight.multiply(stayPeriod.nights());
    }

    private void validate(final Reservation reservation) {
        if (!hasValidPeriod(reservation)) {
            throw new InvalidReservationPeriodException();
        }
        if (!hasSufficientCapacity(reservation)) {
            throw new RoomCapacityExceededException();
        }
        if (!isAvailable(reservation)) {
            throw new RoomNotAvailableException();
        }
    }

    private boolean hasValidPeriod(final Reservation reservation) {
        return !reservation.startsBefore(reservation.timestamp().toLocalDate());
    }

    private boolean hasSufficientCapacity(final Reservation reservation) {
        return reservation.guests() <= type.getCapacity();
    }

    private boolean isAvailable(final Reservation reservation) {
        return reservations.stream()
                .noneMatch(reservation::overlaps);
    }

    private void commit(final Reservation reservation) {
        reservations.add(reservation);
        revenue = revenue.add(reservation.value());
    }

    private void publishEvents(final Reservation reservation) {
        var event = new ReservationRegistered(number, reservation);
        eventListeners.forEach(listener -> listener.accept(event));
    }

    public void addEventListener(final Consumer<ReservationRegistered> listener) {
        eventListeners.add(listener);
    }

    public void removeEventListener(final Consumer<ReservationRegistered> listener) {
        eventListeners.remove(listener);
    }

    public RoomId getId() {
        return id;
    }

    public RoomNumber getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public Money getPricePerNight() {
        return pricePerNight;
    }

    public Money getRevenue() {
        return revenue;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id.value() +
                ", number=" + number.value() +
                ", type=" + type +
                ", pricePerNight=" + pricePerNight +
                ", revenue=" + revenue +
                '}';
    }

}
