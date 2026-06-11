package az.ticketproject.msorder.client;

import az.ticketproject.msorder.client.dto.CourierResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ms-courier", url = "http://localhost:8086/api/v1/couriers")
public interface CourierClient {
    @GetMapping("/available")
    CourierResponse getAvailableCourier();
}