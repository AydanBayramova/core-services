package az.ticketproject.msorder.enums;

public enum OrderStatus {
    ASSIGNED, PICKED_UP, DELIVERED, CANCELLED;

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this) {
            case ASSIGNED -> (nextStatus == PICKED_UP || nextStatus == CANCELLED);
            case PICKED_UP -> (nextStatus == DELIVERED || nextStatus == CANCELLED);
            case DELIVERED, CANCELLED -> false;
        };
    }
}
