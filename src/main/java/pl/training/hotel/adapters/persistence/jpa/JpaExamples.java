package pl.training.hotel.adapters.persistence.jpa;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static pl.training.hotel.domain.RoomType.DOUBLE;

@Profile("examples & !mongo")
@Order(2)
@Component
public class JpaExamples implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(JpaExamples.class.getName());

    private final JpaRoomRepository repository;
    private final TransactionTemplate transactionTemplate;

    public JpaExamples(final JpaRoomRepository repository, final TransactionTemplate transactionTemplate) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        printAvailableRooms();
        printMostExpensiveDoubleRooms();
        printOccupancy();
        printRevenue();
        printAuditingDates();
        showOptimisticLocking();
    }

    private void printAvailableRooms() {
        var checkIn = LocalDate.now().plusDays(3);
        var checkOut = LocalDate.now().plusDays(5);
        LOGGER.info("Double rooms available from %s to %s (JPQL + not exists):".formatted(checkIn, checkOut));
        repository.findAvailable(DOUBLE, checkIn, checkOut)
                .forEach(room -> LOGGER.info(room.getNumber() + " " + room.getPricePerNight()));
    }

    private void printMostExpensiveDoubleRooms() {
        LOGGER.info("Double rooms count: %d, first page sorted by price desc (derived query + paging):"
                .formatted(repository.countByType(DOUBLE)));
        var pageRequest = PageRequest.of(0, 2, DESC, "pricePerNight.amount");
        var page = repository.findByType(DOUBLE, pageRequest);
        page.forEach(room -> LOGGER.info(room.getNumber() + " " + room.getPricePerNight()));
        LOGGER.info("Page %d of %d".formatted(page.getNumber() + 1, page.getTotalPages()));
    }

    private void printOccupancy() {
        LOGGER.info("Occupancy report (constructor expression + group by):");
        repository.findOccupancy()
                .forEach(occupancy -> LOGGER.info(occupancy.toString()));
    }

    private void printRevenue() {
        LOGGER.info("Total revenue (native query): %s PLN".formatted(repository.sumRevenue("PLN")));
    }

    private void printAuditingDates() {
        LOGGER.info("Auditing dates (Spring Data auditing):");
        repository.findAll()
                .forEach(room -> LOGGER.info("%s created %s, modified %s, version %d"
                        .formatted(room.getNumber(), room.getCreatedAt(), room.getLastModifiedAt(), room.getVersion())));
    }

    private void showOptimisticLocking() {
        LOGGER.info("Optimistic locking (@Version):");
        var staleRoom = repository.findAll().getFirst();
        transactionTemplate.executeWithoutResult(status -> repository.findById(staleRoom.getId())
                .orElseThrow()
                .setPricePerNight(new MoneyEmbeddable(BigDecimal.valueOf(999), "PLN")));
        staleRoom.setPricePerNight(new MoneyEmbeddable(BigDecimal.valueOf(1), "PLN"));
        try {
            repository.save(staleRoom);
        } catch (ObjectOptimisticLockingFailureException exception) {
            LOGGER.info("Room %s was modified by another transaction: %s"
                    .formatted(staleRoom.getNumber(), exception.getClass().getSimpleName()));
        }
    }

}
