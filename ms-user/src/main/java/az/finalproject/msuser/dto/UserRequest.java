package az.finalproject.msuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotBlank(message = "Name cannot null")
                          String firstName,

                          @NotBlank(message = "Surname cannot null")
                          String lastName,

                          @Email(message = "Enter the correct email")
                          @NotBlank(message = "Email cannot null")
                          String email,

                          @NotBlank(message = "Phone cannot null")
                          @Size(min = 10, max = 15)
                          String phoneNumber) {
}
