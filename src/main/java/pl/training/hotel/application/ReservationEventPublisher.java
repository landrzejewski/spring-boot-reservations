package pl.training.hotel.application;

public interface ReservationEventPublisher {

    void publish(ReservationAdded reservationAdded);

}
