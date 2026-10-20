package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MongoRoomRepository extends MongoRepository<RoomDocument, String> {

    Optional<RoomDocument> findByNumber(String number);

    Page<RoomDocument> findByType(String type, Pageable pageable);

    long countByType(String type);

    @Query("{ 'reservations': { $elemMatch: { 'guests': { $gte: ?0 } } } }")
    List<RoomDocument> findWithReservationForAtLeast(int guests, Sort sort);

    @Aggregation(pipeline = {
            "{ $project: { _id: 0, number: 1, reservations: { $size: '$reservations' }, guests: { $sum: '$reservations.guests' } } }",
            "{ $sort: { number: 1 } }"
    })
    List<MongoRoomOccupancy> findOccupancy();

}
