package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "rooms")
public record RoomDocument(@Id String id, String number, String type,
                           @Field(targetType = FieldType.DECIMAL128) BigDecimal pricePerNight,
                           String currencyCode, List<ReservationDocument> reservations) {
}
