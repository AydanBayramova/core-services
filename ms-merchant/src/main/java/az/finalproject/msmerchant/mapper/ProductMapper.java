package az.finalproject.msmerchant.mapper;


import az.finalproject.msmerchant.dto.request.ProductRequest;
import az.finalproject.msmerchant.dto.response.ProductResponse;
import az.finalproject.msmerchant.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);
}