package az.ticketproject.msorder.dto;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String category,
        String addressText,
        Double latitude,
        Double longitude
       // MerchantStatus status
) {}
