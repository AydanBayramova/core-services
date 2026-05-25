package az.finalproject.mscourier.dto;

import az.finalproject.mscourier.enums.CourierStatus;
import az.finalproject.mscourier.enums.VehicleType;

import java.util.UUID;

public record CourierResponse(
        UUID id,
        UUID userAuthId,
        VehicleType vehicleType,
        CourierStatus status,
        Double currentLat,
        Double currentLon
) {}
