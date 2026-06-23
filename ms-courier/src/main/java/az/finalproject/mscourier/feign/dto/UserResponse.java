package az.finalproject.mscourier.feign.dto;


import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id,
                           String firstName,
                           String lastName,
                           String email,
                           String phoneNumber) {
}
