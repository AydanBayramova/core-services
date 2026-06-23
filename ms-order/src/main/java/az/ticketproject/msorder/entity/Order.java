package az.ticketproject.msorder.entity;

import az.ticketproject.msorder.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String orderNumber;

    private UUID customerId;
    private UUID merchantId;
    private UUID courierId;

    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private String deliveryAddressSnapshot;

    @CreatedDate
    private LocalDateTime createdAt;
}
