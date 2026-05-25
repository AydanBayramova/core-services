package az.finalproject.mscourier.service;

import az.finalproject.mscourier.dto.CourierRequest;
import az.finalproject.mscourier.dto.CourierResponse;
import az.finalproject.mscourier.entity.Courier;
import az.finalproject.mscourier.enums.CourierStatus;
import az.finalproject.mscourier.exception.AlreadyExistException;
import az.finalproject.mscourier.exception.CourierNotFoundException;
import az.finalproject.mscourier.exception.NoCourierAvailableException;
import az.finalproject.mscourier.exception.UserNotFoundException;
import az.finalproject.mscourier.feign.UserClient;
import az.finalproject.mscourier.mapper.CourierMapper;
import az.finalproject.mscourier.repository.CourierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {

    private final CourierRepository courierRepository;
    private final UserClient userClient;
    private final CourierMapper courierMapper;

    @Transactional
    public void createCourier(CourierRequest request) {
        try {
            userClient.getUserById(request.userId());
        } catch (Exception e) {
            throw new UserNotFoundException("User not found in ms-user system!");
        }

        if (courierRepository.existsByUserAuthId(request.userId())) {
            throw new AlreadyExistException("User already has a courier account!");
        }
        Courier courier = Courier.builder()
                .userAuthId(request.userId())
                .vehicleType(request.vehicleType())
                .status(CourierStatus.OFFLINE)
                .build();

        courierRepository.save(courier);
        log.info("Courier created for user ID: {}", request.userId());
    }

    public CourierResponse getAvailableCourier() {
        return courierRepository.findFirstByStatus(CourierStatus.FREE)
                .map(courierMapper::toResponse)
                .orElseThrow(() -> new NoCourierAvailableException("No free couriers available at the moment"));
    }

    @Transactional
    public void updateStatus(UUID courierId, CourierStatus newStatus) {
        Courier courier = findEntityById(courierId);
        courier.setStatus(newStatus);
        courierRepository.save(courier);
        log.info("Courier {} status updated to {}", courierId, newStatus);
    }

    @Transactional
    public void updateLocation(UUID courierId, Double lat, Double lon) {
        Courier courier = findEntityById(courierId);
        courier.setCurrentLat(lat);
        courier.setCurrentLon(lon);
        courierRepository.save(courier);
    }

    public CourierResponse getCourierById(UUID id) {
        return courierMapper.toResponse(findEntityById(id));
    }

    private Courier findEntityById(UUID id) {
        return courierRepository.findById(id)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found with id: " + id));
    }
}
