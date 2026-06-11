package az.ticketproject.msorder.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderFinishedEvent(
        UUID orderId,
        UUID courierId,
        BigDecimal deliveryFee,
        BigDecimal totalAmount,
        String status
) {}