package az.finalproject.msuser.controller;

import az.finalproject.msuser.dto.AddressRequest;
import az.finalproject.msuser.dto.AddressResponse;
import az.finalproject.msuser.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;


    @ResponseStatus(CREATED)
    public void addAddress(@PathVariable UUID userId, @Valid @RequestBody AddressRequest addressRequest) {
        addressService.addAddress(userId, addressRequest);
    }

    @GetMapping
    public List<AddressResponse> getUserAddresses(@PathVariable UUID userId) {
        return addressService.getUserAddresses(userId);
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteAddress(@PathVariable UUID userId, @PathVariable UUID addressId) {
        addressService.deleteAddress(userId, addressId);
    }

    @GetMapping("/{addressId}/verify")
    public AddressResponse verifyAddress(
            @PathVariable UUID userId,
            @PathVariable UUID addressId) {
        return addressService.verifyUserAndAddress(userId, addressId);
    }

}
