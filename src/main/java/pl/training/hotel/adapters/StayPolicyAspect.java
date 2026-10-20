package pl.training.hotel.adapters;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import pl.training.hotel.domain.RoomNumber;
import pl.training.hotel.domain.StayPeriod;

@Aspect
public class StayPolicyAspect {

    private final StayPolicyProperties properties;

    public StayPolicyAspect(final StayPolicyProperties properties) {
        this.properties = properties;
    }

    @Before(value = "execution(* pl.training.hotel.application.AddReservationUseCase.handle(..)) && args(roomNumber, stayPeriod, guests)",
            argNames = "roomNumber,stayPeriod,guests")
    public void checkStayLength(final RoomNumber roomNumber, final StayPeriod stayPeriod, final int guests) {
        if (stayPeriod.nights() > properties.maxNights()) {
            throw new StayPolicyViolationException("Stay in room %s exceeds %d night(s)"
                    .formatted(roomNumber.value(), properties.maxNights()));
        }
    }

}
