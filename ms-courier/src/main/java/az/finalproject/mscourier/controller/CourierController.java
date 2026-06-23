package az.finalproject.mscourier.controller;

import az.finalproject.mscourier.dto.CourierRequest;
import az.finalproject.mscourier.dto.CourierResponse;
import az.finalproject.mscourier.enums.CourierStatus;
import az.finalproject.mscourier.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourier(@Valid @RequestBody CourierRequest request) {
        courierService.createCourier(request);
    }

    @GetMapping("/{id}")
    public CourierResponse getById(@PathVariable UUID id) {
        return courierService.getCourierById(id);
    }

    @GetMapping("/available")
    public CourierResponse getAvailable() {
        return courierService.getAvailableCourier();
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable UUID id, @RequestParam CourierStatus status) {
        courierService.updateStatus(id, status);
    }

    @PatchMapping("/{id}/location")
    public void updateLocation(
            @PathVariable UUID id,
            @RequestParam Double lat,
            @RequestParam Double lon) {
        courierService.updateLocation(id, lat, lon);
    }
}