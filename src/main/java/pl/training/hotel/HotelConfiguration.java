package pl.training.hotel;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pl.training.hotel.adapters.*;
import pl.training.hotel.application.*;

import java.util.List;

@EnableJpaAuditing
@EnableConfigurationProperties({RoomNumberProperties.class, StayPolicyProperties.class})
@Configuration
public class HotelConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new SystemDateTimeProvider();
    }

    @Bean
    public RoomNumberGenerator standardRoomNumberGenerator(RoomNumberProperties properties) {
        return new FloorRoomNumberGenerator(properties.roomsPerFloor());
    }

    @Bean
    public RoomNumberGenerator suiteRoomNumberGenerator(RoomNumberProperties properties) {
        return new SequentialRoomNumberGenerator(properties.suiteStartNumber());
    }

    @Order(1)
    @Bean
    public ReservationEventPublisher consoleReservationEventPublisher() {
        return new ConsoleReservationEventPublisher();
    }

    @Profile("dev")
    @Order(2)
    @Bean
    public InMemoryReservationEventPublisher inMemoryReservationEventPublisher() {
        return new InMemoryReservationEventPublisher();
    }

    @Bean
    public StayPolicyAspect stayPolicyAspect(StayPolicyProperties properties) {
        return new StayPolicyAspect(properties);
    }

    @Bean
    public AddRoomUseCase addRoomUseCase(RoomNumberProperties properties, RoomRepository roomRepository) {
        return new AddRoomUseCase(standardRoomNumberGenerator(properties), suiteRoomNumberGenerator(properties), roomRepository);
    }

    @Bean
    public AddReservationUseCase addReservationUseCase(List<ReservationEventPublisher> reservationEventPublishers,
                                                       RoomRepository roomRepository) {
        return new AddReservationUseCase(dateTimeProvider(), reservationEventPublishers, roomRepository);
    }

    @Bean
    public GetRoomsUseCase getRoomsUseCase(RoomRepository roomRepository) {
        return new GetRoomsUseCase(roomRepository);
    }

    @Bean
    public GetRoomUseCase getRoomUseCase(RoomRepository roomRepository) {
        return new GetRoomUseCase(roomRepository);
    }

}
