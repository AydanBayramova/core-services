package az.finalproject.msuser.repository;


import az.finalproject.msuser.dto.AddressResponse;
import az.finalproject.msuser.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    void deleteByIdAndUserId(UUID id, UUID userId);

    List<Address> findByUserId(UUID userId);

    boolean existsByIdAndUserId(UUID addressId, UUID userId);

    Optional<Address> findByIdAndUserId(UUID addressId, UUID userId);
}

