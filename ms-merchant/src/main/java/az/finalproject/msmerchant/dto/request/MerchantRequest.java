package az.finalproject.msmerchant.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MerchantRequest(
        @NotBlank String name,
        @NotBlank String category,
        @NotBlank String addressText,
        Double latitude,
        Double longitude
) {}
