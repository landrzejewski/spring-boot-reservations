package pl.training.hotel.adapters.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.training.hotel.domain.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaRoomRepository extends JpaRepository<RoomEntity, String> {

    @EntityGraph(attributePaths = "reservations")
    Optional<RoomEntity> findByNumber(String number);

    Page<RoomEntity> findByType(RoomType type, Pageable pageable);

    long countByType(RoomType type);

    @Query("""
            select r from Room r
            where r.type = :type
              and not exists (
                select reservation from Reservation reservation
                where reservation.room = r
                  and reservation.checkIn < :checkOut
                  and :checkIn < reservation.checkOut)
            order by r.pricePerNight.amount
            """)
    List<RoomEntity> findAvailable(RoomType type, LocalDate checkIn, LocalDate checkOut);

    @Query("""
            select new pl.training.hotel.adapters.persistence.jpa.RoomOccupancy(r.number, count(reservation), coalesce(sum(reservation.guests), 0L))
            from Room r left join r.reservations reservation
            group by r.number
            order by r.number
            """)
    List<RoomOccupancy> findOccupancy();

    @Query(value = "select coalesce(sum(revenue_amount), 0) from rooms where revenue_currency = :currencyCode", nativeQuery = true)
    BigDecimal sumRevenue(String currencyCode);

}
