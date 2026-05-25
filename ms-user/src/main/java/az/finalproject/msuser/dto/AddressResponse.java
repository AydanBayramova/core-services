package az.finalproject.msuser.dto;

import java.util.UUID;

public record AddressResponse(

        UUID id,
        String title,
        String addressText,
        Double latitude,
        Double longitude
) {}
