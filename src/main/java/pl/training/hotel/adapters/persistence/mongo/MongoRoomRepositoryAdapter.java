package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.application.RoomRepository;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Profile("mongo")
@Repository
public class MongoRoomRepositoryAdapter implements RoomRepository {

    private final MongoTemplate mongoTemplate;
    private final MongoRoomRepositoryMapper mapper;

    public MongoRoomRepositoryAdapter(final MongoTemplate mongoTemplate, final MongoRoomRepositoryMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Room save(final Room room) {
        var savedDocument = mongoTemplate.save(mapper.toDocument(room));
        return mapper.toDomain(savedDocument);
    }

    @Override
    public ResultPage<Room> findAll(final PageSpec pageSpec) {
        var query = new Query().with(PageRequest.of(pageSpec.index(), pageSpec.size(), Sort.by("number")));
        var rooms = mongoTemplate.find(query, RoomDocument.class).stream()
                .map(mapper::toDomain)
                .toList();
        var totalElements = mongoTemplate.count(new Query(), RoomDocument.class);
        var totalPages = (int) Math.ceil((double) totalElements / pageSpec.size());
        return new ResultPage<>(rooms, pageSpec, totalPages);
    }

    @Override
    public Optional<Room> findByNumber(final RoomNumber roomNumber) {
        var query = Query.query(where("number").is(roomNumber.value()));
        return Optional.ofNullable(mongoTemplate.findOne(query, RoomDocument.class))
                .map(mapper::toDomain);
    }

}
