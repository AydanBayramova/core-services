package az.finalproject.mscourier.dto;

import az.finalproject.mscourier.enums.VehicleType;

import java.util.UUID;

public record CourierRequest(
        UUID userId,
        VehicleType vehicleType
) {}
