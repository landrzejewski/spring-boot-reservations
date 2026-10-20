package pl.training.hotel.adapters;

import pl.training.hotel.application.DateTimeProvider;

import java.time.ZonedDateTime;

public final class SystemDateTimeProvider implements DateTimeProvider {

    @Override
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now();
    }

}
