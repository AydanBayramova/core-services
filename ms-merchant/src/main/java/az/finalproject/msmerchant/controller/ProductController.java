package az.finalproject.msmerchant.controller;

import az.finalproject.msmerchant.dto.response.ProductResponse;
import az.finalproject.msmerchant.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PatchMapping("/{id}/price")
    public void updatePrice(@PathVariable UUID id, @RequestParam BigDecimal newPrice) {
        productService.updateProductPrice(id, newPrice);
    }

    @GetMapping("/{id}/verify")
    public ProductResponse verifyProduct(@PathVariable UUID id, @RequestParam BigDecimal price) {
        return productService.verifyProductAndPrice(id, price);
    }
}
