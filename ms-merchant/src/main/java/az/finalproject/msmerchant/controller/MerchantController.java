package az.finalproject.msmerchant.controller;


import az.finalproject.msmerchant.dto.request.MerchantRequest;
import az.finalproject.msmerchant.dto.request.ProductRequest;
import az.finalproject.msmerchant.dto.response.MerchantResponse;
import az.finalproject.msmerchant.service.MerchantService;
import az.finalproject.msmerchant.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final ProductService productService;

    @PostMapping
    public MerchantResponse createMerchant(@Valid @RequestBody MerchantRequest request) {
        return merchantService.createMerchant(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchantById(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchantById(id));
    }


    @PostMapping("/{id}/products")
    public void addProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        productService.addProductToMerchant(id, request);
    }


    @GetMapping("/{id}/products")
    public MerchantResponse getProductsByMerchant(@PathVariable UUID id) {
       return productService.getProductsByMerchant(id);
    }
}
