package az.finalproject.msmerchant.dto.response;

public record GeocodingResponse(
        String lat,
        String lon,
        String display_name
) {}