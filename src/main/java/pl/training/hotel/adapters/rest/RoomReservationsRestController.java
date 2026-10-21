package pl.training.hotel.adapters.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;
import pl.training.hotel.application.AddReservationUseCase;
import pl.training.hotel.domain.RoomNumber;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("api/rooms/{number}/reservations")
@RestController
class RoomReservationsRestController {

    private final AddReservationUseCase addReservationUseCase;

    RoomReservationsRestController(AddReservationUseCase addReservationUseCase) {
        this.addReservationUseCase = addReservationUseCase;
    }


    @ResponseStatus(CREATED)
    @PostMapping
    AddReservationResponse addReservation(@PathVariable String number,
                                          @RequestBody AddReservationRequest request) {
        var reservationId = addReservationUseCase.handle(new RoomNumber(number), request.stayPeriod(), request.guests());
        return AddReservationResponse.from(reservationId);
    }

}
