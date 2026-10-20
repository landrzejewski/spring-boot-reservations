package pl.training.hotel.adapters.persistence.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.application.RoomRepository;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

import java.util.Optional;

@Profile("!mongo")
@Repository
public class JpaRoomRepositoryAdapter implements RoomRepository {

    private final JpaRoomRepository repository;
    private final JpaRoomRepositoryMapper mapper;

    public JpaRoomRepositoryAdapter(final JpaRoomRepository repository, final JpaRoomRepositoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Room save(final Room room) {
        var roomEntity = repository.findById(room.getId().value().toString())
                .orElseGet(RoomEntity::new);
        mapper.update(roomEntity, room);
        var savedEntity = repository.save(roomEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public ResultPage<Room> findAll(final PageSpec pageSpec) {
        var pageRequest = mapper.toEntity(pageSpec);
        var roomEntityPage = repository.findAll(pageRequest);
        return mapper.toDomain(roomEntityPage);
    }

    @Override
    public Optional<Room> findByNumber(final RoomNumber roomNumber) {
        return repository.findByNumber(roomNumber.value())
                .map(mapper::toDomain);
    }

}
