package az.finalproject.msuser.mapper;

import az.finalproject.msuser.dto.UserRequest;
import az.finalproject.msuser.dto.UserResponse;
import az.finalproject.msuser.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}