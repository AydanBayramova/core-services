package az.finalproject.msuser.mapper;



import az.finalproject.msuser.dto.AddressRequest;
import az.finalproject.msuser.dto.AddressResponse;
import az.finalproject.msuser.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressRequest request);

    AddressResponse toResponse(Address address);
    List<AddressResponse> toResponseList(List<Address> address);
}
