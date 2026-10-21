package pl.training.hotel.adapters.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.training.common.PageSpec;
import pl.training.common.ResultPage;
import pl.training.hotel.application.AddRoomUseCase;
import pl.training.hotel.application.GetRoomUseCase;
import pl.training.hotel.application.GetRoomsUseCase;
import pl.training.hotel.domain.RoomNumber;


@RequestMapping("api/rooms")
@RestController
class RoomsRestController {

    private final AddRoomUseCase addRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final GetRoomsUseCase getRoomsUseCase;

    RoomsRestController(AddRoomUseCase addRoomUseCase, GetRoomUseCase getRoomUseCase, GetRoomsUseCase getRoomsUseCase) {
        this.addRoomUseCase = addRoomUseCase;
        this.getRoomUseCase = getRoomUseCase;
        this.getRoomsUseCase = getRoomsUseCase;
    }


    @PostMapping
    ResponseEntity<RoomResponse> addRoom( @RequestBody AddRoomRequest request, UriComponentsBuilder uriBuilder) {
        var room = addRoomUseCase.handle(request.type(), request.price());
        var location = uriBuilder.path("/api/rooms/{number}")
                .buildAndExpand(room.getNumber().value())
                .toUri();
        return ResponseEntity.created(location)
                .body(RoomResponse.from(room));
    }


    @GetMapping("{number}")
    RoomResponse getRoom(@PathVariable  String number) {
        var room = getRoomUseCase.handle(new RoomNumber(number));
        return RoomResponse.from(room);
    }


    @GetMapping
    ResultPage<RoomSummaryResponse> getRooms(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return getRoomsUseCase.handle(new PageSpec(page, size))
                .map(RoomSummaryResponse::from);
    }

}
