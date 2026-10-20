package pl.training.hotel.adapters.persistence.mongo;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ReservationDocument(String id, Instant placedAt, LocalDate checkIn, LocalDate checkOut, int guests,
                                  @Field(targetType = FieldType.DECIMAL128) BigDecimal price) {
}
