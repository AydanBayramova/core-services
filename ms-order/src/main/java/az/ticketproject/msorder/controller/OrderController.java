package az.ticketproject.msorder.controller;

import az.ticketproject.msorder.dto.OrderRequest;
import az.ticketproject.msorder.dto.OrderResponse;
import az.ticketproject.msorder.dto.StatusUpdateRequest;
import az.ticketproject.msorder.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getCustomerOrders(@PathVariable UUID customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @GetMapping("/courier/{courierId}/active")
    public OrderResponse getCourierActiveOrder(@PathVariable UUID courierId) {
        return orderService.getActiveOrderByCourierId(courierId);
    }
}
