package pl.training.hotel.adapters.persistence.jpa;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Table(name = "reservations")
@Entity(name = "Reservation")
public class ReservationEntity {

    @Id
    private String id;
    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RoomEntity room;
    private ZonedDateTime placedAt;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
    @AttributeOverride(name = "currencyCode", column = @Column(name = "price_currency"))
    private MoneyEmbeddable price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public ZonedDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(ZonedDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public MoneyEmbeddable getPrice() {
        return price;
    }

    public void setPrice(MoneyEmbeddable price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
