package az.ticketproject.msorder.client.dto;

import java.util.UUID;

public record CourierResponse(
        UUID id,
        UUID userAuthId,
        VehicleType vehicleType,
        CourierStatus status,
        Double currentLat,
        Double currentLon
) {}