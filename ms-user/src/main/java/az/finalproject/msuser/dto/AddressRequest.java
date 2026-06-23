package az.finalproject.msuser.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(

        @NotBlank(message = "Title cannot null")
        String title,

        @NotBlank(message = "AddressText cannot null")
        String addressText,

        Double latitude,

        Double longitude
) {
}