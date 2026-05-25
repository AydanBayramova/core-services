package az.finalproject.msmerchant.repository;

import az.finalproject.msmerchant.entity.Merchant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    @EntityGraph(attributePaths = {"products"})
    Optional<Merchant> findWithProductsById(UUID id);
}
