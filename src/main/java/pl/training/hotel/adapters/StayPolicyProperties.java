package pl.training.hotel.adapters;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hotel.stay-policy")
public record StayPolicyProperties(int maxNights) {
}
