package az.finalproject.msmerchant.service;

import az.finalproject.msmerchant.dto.Coordinate;
import az.finalproject.msmerchant.dto.request.MerchantRequest;
import az.finalproject.msmerchant.dto.response.MerchantResponse;
import az.finalproject.msmerchant.exception.InvalidAddressException;
import az.finalproject.msmerchant.exception.MerchantNotFound;
import az.finalproject.msmerchant.mapper.MerchantMapper;
import az.finalproject.msmerchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static az.finalproject.msmerchant.enums.MerchantStatus.OPEN;


@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final GeocodingService geocodingService;
    private final MerchantMapper merchantMapper;

    public MerchantResponse createMerchant(MerchantRequest request) {
        var merchantEntity = merchantMapper.toEntity(request);

        if (merchantEntity.getLatitude() == null || merchantEntity.getLongitude() == null) {
            Coordinate coordinate = geocodingService.fetchCoordinates(request.addressText());
            if (coordinate == null) {
                throw new InvalidAddressException("Could not find coordinates for the provided address: " + request.addressText());
            }
                merchantEntity.setLatitude(coordinate.lat());
                merchantEntity.setLongitude(coordinate.lon());
        }
        merchantEntity.setStatus(OPEN);
        return merchantMapper.toResponse(merchantRepository.save(merchantEntity));
    }

    public MerchantResponse getMerchantById(UUID id) {

        var merchant = merchantRepository.findById(id).orElseThrow(
                () -> new MerchantNotFound("Merchant cannot find"));
        return merchantMapper.toResponse(merchant);
    }
}
