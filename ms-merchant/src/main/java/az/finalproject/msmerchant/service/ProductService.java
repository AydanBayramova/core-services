package az.finalproject.msmerchant.service;


import az.finalproject.msmerchant.dto.request.ProductRequest;
import az.finalproject.msmerchant.dto.response.MerchantResponse;
import az.finalproject.msmerchant.dto.response.ProductResponse;
import az.finalproject.msmerchant.exception.MerchantNotFound;
import az.finalproject.msmerchant.exception.PriceMismatchException;
import az.finalproject.msmerchant.exception.ProductNotFoundException;
import az.finalproject.msmerchant.mapper.MerchantMapper;
import az.finalproject.msmerchant.mapper.ProductMapper;
import az.finalproject.msmerchant.repository.MerchantRepository;
import az.finalproject.msmerchant.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;


    @Transactional
    public void addProductToMerchant(UUID merchantId, ProductRequest request) {

        var merchant = merchantRepository.findById(merchantId).orElseThrow(
                () -> new MerchantNotFound("Merchant cannot find"));

        var product = productMapper.toEntity(request);

        product.setMerchant(merchant);
        merchant.getProducts().add(product);
        merchantRepository.save(merchant);
    }

    public MerchantResponse getProductsByMerchant(UUID merchantId) {
        var merchant = merchantRepository.findWithProductsById(merchantId)
                .orElseThrow(() -> new MerchantNotFound("Merchant cannot find: " + merchantId));
        return merchantMapper.toResponse(merchant);
    }

    public void updateProductPrice(UUID productId, BigDecimal newPrice) {

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found" + productId));
        product.setPrice(newPrice);
        productRepository.save(product);
    }

    public ProductResponse verifyProductAndPrice(UUID productId, BigDecimal expectedPrice) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot found" + productId));
        if (product.getPrice().compareTo(expectedPrice) != 0) {
            throw new PriceMismatchException("Price does not match product price");
        }
        return productMapper.toResponse(product);
    }
}
