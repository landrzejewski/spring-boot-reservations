package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.application.RoomRepository;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

import java.util.Optional;

@Profile("mongo & !mongo-template")
@Repository
public class MongoRoomRepositoryAdapter implements RoomRepository {

    private final MongoRoomRepository repository;
    private final MongoRoomRepositoryMapper mapper;

    public MongoRoomRepositoryAdapter(final MongoRoomRepository repository, final MongoRoomRepositoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Room save(final Room room) {
        var savedDocument = repository.save(mapper.toDocument(room));
        return mapper.toDomain(savedDocument);
    }

    @Override
    public ResultPage<Room> findAll(final PageSpec pageSpec) {
        var pageRequest = mapper.toDocument(pageSpec);
        var roomDocumentPage = repository.findAll(pageRequest);
        return mapper.toDomain(roomDocumentPage);
    }

    @Override
    public Optional<Room> findByNumber(final RoomNumber roomNumber) {
        return repository.findByNumber(roomNumber.value())
                .map(mapper::toDomain);
    }

}
