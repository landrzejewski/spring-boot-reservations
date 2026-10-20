package pl.training.hotel.adapters.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import pl.training.hotel.adapters.RoomNumberProperties;
import pl.training.hotel.adapters.StayPolicyProperties;

import java.util.Map;

@Component
class HotelInfoContributor implements InfoContributor {

    private final RoomNumberProperties roomNumberProperties;
    private final StayPolicyProperties stayPolicyProperties;

    HotelInfoContributor(RoomNumberProperties roomNumberProperties, StayPolicyProperties stayPolicyProperties) {
        this.roomNumberProperties = roomNumberProperties;
        this.stayPolicyProperties = stayPolicyProperties;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("hotel", Map.of(
                "roomsPerFloor", roomNumberProperties.roomsPerFloor(),
                "suiteStartNumber", roomNumberProperties.suiteStartNumber(),
                "maxNights", stayPolicyProperties.maxNights()
        ));
    }

}
