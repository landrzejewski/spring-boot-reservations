package pl.training.hotel.domain;

public enum RoomType {

    SINGLE(1),
    DOUBLE(2),
    SUITE(4);

    private final int capacity;

    RoomType(final int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

}
