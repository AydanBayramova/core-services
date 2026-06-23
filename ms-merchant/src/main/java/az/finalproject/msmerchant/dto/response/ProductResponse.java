package az.finalproject.msmerchant.dto.response;


import az.finalproject.msmerchant.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status
) {}
