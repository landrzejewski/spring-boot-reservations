package pl.training.hotel.adapters;

import org.springframework.stereotype.Component;
import pl.training.hotel.application.DateTimeProvider;

import java.time.ZonedDateTime;

@Component
public final class SystemDateTimeProvider implements DateTimeProvider {

    @Override
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now();
    }

}
