package az.ticketproject.msorder.client;

import az.ticketproject.msorder.client.dto.ProductResponse;
import az.ticketproject.msorder.dto.MerchantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "ms-merchant", url = "http://localhost:8085/api/v1") // Ana yolu bura yazırıq
public interface MerchantClient {

    @GetMapping("/merchants/{id}")
    MerchantResponse getMerchantById(@PathVariable UUID id);

    @GetMapping("/products/{id}/verify")
    ProductResponse verifyProduct(@PathVariable UUID id, @RequestParam BigDecimal price);
}
