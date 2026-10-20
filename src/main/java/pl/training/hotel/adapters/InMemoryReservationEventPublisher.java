package pl.training.hotel.adapters;

import pl.training.hotel.application.ReservationAdded;
import pl.training.hotel.application.ReservationEventPublisher;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryReservationEventPublisher implements ReservationEventPublisher {

    private final List<ReservationAdded> publishedEvents = new ArrayList<>();

    @Override
    public synchronized void publish(final ReservationAdded reservationAdded) {
        publishedEvents.add(reservationAdded);
    }

    public synchronized List<ReservationAdded> getPublishedEvents() {
        return new ArrayList<>(publishedEvents);
    }

}
