package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Profile("mongo")
@Order(3)
@Component
public class MongoRepositoryExamples implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(MongoRepositoryExamples.class.getName());

    private final MongoRoomRepository repository;

    public MongoRepositoryExamples(final MongoRoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        printDoubleRooms();
        printRoomsWithReservations();
        printOccupancy();
    }

    private void printDoubleRooms() {
        var page = repository.findByType("DOUBLE", PageRequest.of(0, 5, Sort.by("pricePerNight").descending()));
        LOGGER.info("Double rooms (derived query with paging): %d of %d".formatted(page.getNumberOfElements(), repository.countByType("DOUBLE")));
        page.forEach(room -> LOGGER.info(room.number() + " " + room.pricePerNight()));
    }

    private void printRoomsWithReservations() {
        LOGGER.info("Rooms with at least one reservation for 2+ guests (@Query):");
        repository.findWithReservationForAtLeast(2, Sort.by("number"))
                .forEach(room -> LOGGER.info(room.number() + " " + room.reservations().size() + " reservation(s)"));
    }

    private void printOccupancy() {
        LOGGER.info("Room occupancy (@Aggregation):");
        repository.findOccupancy()
                .forEach(occupancy -> LOGGER.info(occupancy.toString()));
    }

}
