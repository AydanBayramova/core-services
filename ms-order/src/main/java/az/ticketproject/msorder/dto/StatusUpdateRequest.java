package az.ticketproject.msorder.dto;

import az.ticketproject.msorder.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull OrderStatus status
) {
}