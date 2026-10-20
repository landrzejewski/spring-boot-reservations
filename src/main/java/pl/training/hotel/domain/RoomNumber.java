package pl.training.hotel.domain;

public record RoomNumber(String value) {

    private static final String NUMBER_PATTERN = "\\d{3,5}";

    public RoomNumber {
        if (!value.matches(NUMBER_PATTERN)) {
            throw new IllegalArgumentException("Room number must match pattern " + NUMBER_PATTERN);
        }
    }

}
