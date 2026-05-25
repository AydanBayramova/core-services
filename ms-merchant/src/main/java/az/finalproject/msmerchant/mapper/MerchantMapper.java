package az.finalproject.msmerchant.mapper;


import az.finalproject.msmerchant.dto.request.MerchantRequest;
import az.finalproject.msmerchant.dto.response.MerchantResponse;
import az.finalproject.msmerchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class}
)
public interface MerchantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "status", ignore = true)
    Merchant toEntity(MerchantRequest request);

    MerchantResponse toResponse(Merchant merchant);
}