package az.finalproject.mscourier.exception.dto;


import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String errorCode,
        String message,
        LocalDateTime timestamp
) {}