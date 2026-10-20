package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Profile("mongo")
@Order(2)
@Component
public class MongoExamples implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(MongoExamples.class.getName());

    private final MongoTemplate mongoTemplate;

    public MongoExamples(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        printRoomsWithReservations();
        raiseSuitePrices();
    }

    private void printRoomsWithReservations() {
        LOGGER.info("Rooms with at least one reservation for 2+ guests (criteria on embedded documents):");
        var query = Query.query(where("reservations").elemMatch(where("guests").gte(2)))
                .with(Sort.by("number"));
        mongoTemplate.find(query, RoomDocument.class)
                .forEach(room -> LOGGER.info(room.number() + " " + room.reservations().size() + " reservation(s)"));
    }

    private void raiseSuitePrices() {
        var query = Query.query(where("type").is("SUITE"));
        var update = new Update().set("pricePerNight", BigDecimal.valueOf(1000));
        var result = mongoTemplate.updateMulti(query, update, RoomDocument.class);
        LOGGER.info("Suite prices updated (updateMulti): %d document(s)".formatted(result.getModifiedCount()));
    }

}
