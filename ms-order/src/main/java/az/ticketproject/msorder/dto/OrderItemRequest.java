package az.ticketproject.msorder.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        UUID productId,
        Integer quantity,
        BigDecimal expectedPrice
) {}