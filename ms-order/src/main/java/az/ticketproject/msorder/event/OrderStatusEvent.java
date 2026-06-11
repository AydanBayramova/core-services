package az.ticketproject.msorder.event;

import java.util.UUID;

public record OrderStatusEvent(
        UUID orderId,
        UUID courierId,
        String status
) {}