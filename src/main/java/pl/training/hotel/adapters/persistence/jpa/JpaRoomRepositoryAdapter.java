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

    @Override
    public Room save(Room room) {
        return null;
    }

    @Override
    public ResultPage<Room> findAll(PageSpec pageSpec) {
        return null;
    }

    @Override
    public Optional<Room> findByNumber(RoomNumber roomNumber) {
        return Optional.empty();
    }

}
