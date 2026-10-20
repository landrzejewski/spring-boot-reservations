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

public class HashMapRoomRepository implements RoomRepository {

    private final Map<RoomNumber, Room> data = new HashMap<>();

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
