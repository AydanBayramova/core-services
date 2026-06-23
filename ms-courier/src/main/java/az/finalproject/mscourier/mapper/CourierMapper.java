package az.finalproject.mscourier.mapper;
import az.finalproject.mscourier.dto.CourierRequest;
import az.finalproject.mscourier.dto.CourierResponse;
import az.finalproject.mscourier.entity.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourierMapper {

    CourierResponse toResponse(Courier courier);

    @Mapping(target = "userAuthId", source = "userId")
    @Mapping(target = "status", constant = "OFFLINE")
    Courier toEntity(CourierRequest request);
}
