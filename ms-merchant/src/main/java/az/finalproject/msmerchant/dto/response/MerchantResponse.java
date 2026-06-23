package az.finalproject.msmerchant.dto.response;


import az.finalproject.msmerchant.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String category,
        String addressText,
        Double latitude,
        Double longitude,
        MerchantStatus status
) {}