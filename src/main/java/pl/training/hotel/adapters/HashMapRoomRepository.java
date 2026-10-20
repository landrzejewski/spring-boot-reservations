package pl.training.hotel.adapters;

import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.application.RoomRepository;
import pl.training.hotel.domain.Room;
import pl.training.hotel.domain.RoomNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class HashMapRoomRepository implements RoomRepository {

    private static final Logger LOGGER = Logger.getLogger(HashMapRoomRepository.class.getName());

    private final Map<RoomNumber, Room> data = new HashMap<>();

    public void init() {
        LOGGER.info("In-memory room repository initialized");
    }

    public synchronized void destroy() {
        LOGGER.info("In-memory room repository closed, %d room(s) discarded".formatted(data.size()));
    }

    @Override
    public synchronized Room save(final Room room) {
        data.put(room.getNumber(), room);
        return room;
    }

    @Override
    public synchronized ResultPage<Room> findAll(final PageSpec pageSpec) {
        var rooms = new ArrayList<>(data.values());
        int startIndex = Math.min(pageSpec.index() * pageSpec.size(), rooms.size());
        int endIndex = Math.min(startIndex + pageSpec.size(), rooms.size());
        var content = rooms.subList(startIndex, endIndex);
        int totalPages = (int) Math.ceil((double) rooms.size() / pageSpec.size());
        return new ResultPage<>(content, pageSpec, totalPages);
    }

    @Override
    public synchronized Optional<Room> findByNumber(final RoomNumber roomNumber) {
        var room = data.get(roomNumber);
        return Optional.ofNullable(room);
    }

}
