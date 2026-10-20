package pl.training.hotel.adapters.persistence.jpa;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.training.hotel.domain.RoomType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "rooms")
@Entity(name = "Room")
public class RoomEntity {

    @Id
    private String id;
    @Version
    private Long version;
    @Column(unique = true, nullable = false, length = 5)
    private String number;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
    @AttributeOverride(name = "currencyCode", column = @Column(name = "price_currency"))
    private MoneyEmbeddable pricePerNight;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "revenue_amount"))
    @AttributeOverride(name = "currencyCode", column = @Column(name = "revenue_currency"))
    private MoneyEmbeddable revenue;
    @OrderBy("checkIn")
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationEntity> reservations = new ArrayList<>();
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastModifiedAt;

    public void addReservation(ReservationEntity reservation) {
        reservation.setRoom(this);
        reservations.add(reservation);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public MoneyEmbeddable getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(MoneyEmbeddable pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public MoneyEmbeddable getRevenue() {
        return revenue;
    }

    public void setRevenue(MoneyEmbeddable revenue) {
        this.revenue = revenue;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
