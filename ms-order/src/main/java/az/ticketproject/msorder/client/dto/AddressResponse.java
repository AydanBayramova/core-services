package az.ticketproject.msorder.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AddressResponse(

        UUID id,
        String title,
        @JsonProperty("addressText") String addressText,
        @JsonProperty("latitude") Double latitude,
        @JsonProperty("longitude") Double longitude
) {}