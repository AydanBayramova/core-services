package az.ticketproject.msorder.dto;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
        UUID customerId,
        UUID merchantId,
        UUID addressId,
        List<OrderItemRequest> items
) {}
