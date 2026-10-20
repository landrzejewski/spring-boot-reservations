package pl.training.hotel.adapters;

public final class StayPolicyViolationException extends RuntimeException {

    public StayPolicyViolationException(final String message) {
        super(message);
    }

}
