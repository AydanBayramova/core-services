package az.finalproject.mscourier.repository;

import az.finalproject.mscourier.entity.Courier;
import az.finalproject.mscourier.enums.CourierStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourierRepository extends JpaRepository<Courier, UUID> {

    Optional<Courier> findFirstByStatus(CourierStatus status);

    boolean existsByUserAuthId(UUID userAuthId);
}