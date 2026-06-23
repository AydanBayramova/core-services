package az.ticketproject.msorder.repository;

import az.ticketproject.msorder.entity.Order;
import az.ticketproject.msorder.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
  List<Order> findAllByCustomerIdOrderByCreatedAtDesc(UUID customerId);


  Optional<Order> findFirstByCourierIdAndStatusIn(UUID courierId, List<OrderStatus> statuses);
}
