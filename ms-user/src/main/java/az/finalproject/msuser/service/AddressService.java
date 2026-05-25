package az.finalproject.msuser.service;

import az.finalproject.msuser.dto.AddressRequest;
import az.finalproject.msuser.dto.AddressResponse;
import az.finalproject.msuser.exceptions.AddressNotFound;
import az.finalproject.msuser.exceptions.UnauthorizedException;
import az.finalproject.msuser.mapper.AddressMapper;
import az.finalproject.msuser.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;

    public void addAddress(UUID id, AddressRequest addressRequest) {
        var user = userService.findUserById(id);
        var addressEntity = addressMapper.toEntity(addressRequest);
        addressEntity.setUser(user);
        user.getAddresses().add(addressEntity);
        addressRepository.save(addressEntity);
    }

    public List<AddressResponse> getUserAddresses(UUID userId) {
        userService.findUserById(userId);
        return addressMapper.toResponseList(addressRepository.findByUserId(userId));
    }

    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        if (!addressRepository.existsByIdAndUserId(addressId, userId)) {
            throw new AddressNotFound("Address not found for this user");
        }
        addressRepository.deleteByIdAndUserId(addressId, userId);
    }

    public AddressResponse verifyUserAndAddress(UUID userId, UUID addressId) {
        var address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new AddressNotFound("Address not found for this user or access denied"));
        return addressMapper.toResponse(address);
    }

}
