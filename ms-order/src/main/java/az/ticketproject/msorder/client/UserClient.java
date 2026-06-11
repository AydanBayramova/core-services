package az.ticketproject.msorder.client;

import az.ticketproject.msorder.client.dto.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-user", url = "http://localhost:8080/api/v1/users")
public interface UserClient {
    @GetMapping("/{userId}/addresses/{addressId}/verify")
    AddressResponse verifyAddress(@PathVariable UUID userId, @PathVariable UUID addressId);

}
