package pl.training.common.aop;

public final class ThrottlingException extends RuntimeException {

    public ThrottlingException(final String message) {
        super(message);
    }

}
