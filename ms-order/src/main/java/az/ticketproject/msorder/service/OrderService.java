package az.ticketproject.msorder.service;

import az.ticketproject.msorder.client.CourierClient;
import az.ticketproject.msorder.client.MerchantClient;
import az.ticketproject.msorder.client.UserClient;
import az.ticketproject.msorder.client.dto.CourierResponse;
import az.ticketproject.msorder.dto.OrderRequest;
import az.ticketproject.msorder.dto.OrderResponse;
import az.ticketproject.msorder.entity.Order;
import az.ticketproject.msorder.entity.OrderItem;
import az.ticketproject.msorder.enums.OrderStatus;
import az.ticketproject.msorder.event.OrderFinishedEvent;
import az.ticketproject.msorder.event.OrderStatusEvent;
import az.ticketproject.msorder.exception.IllegalStatusTransitionException;
import az.ticketproject.msorder.exception.OrderNotFoundException;
import az.ticketproject.msorder.mapper.OrderMapper;
import az.ticketproject.msorder.repository.OrderRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final MerchantClient merchantClient;
    private final CourierClient courierClient;
    private final OrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        var address = userClient.verifyAddress(request.customerId(), request.addressId());
        log.info("Address verified for user: {}. Address: {}", request.customerId(), address.addressText());
        var merchant = merchantClient.getMerchantById(request.merchantId());
        double distance = calculateDistance(
                address.latitude(), address.longitude(),
                merchant.latitude(), merchant.longitude()
        );
        BigDecimal deliveryFee = BigDecimal.valueOf(Math.max(1.50, distance * 0.70))
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Distance: {} km, Calculated Delivery Fee: {} AZN", distance, deliveryFee);

        List<OrderItem> orderItems = request.items().stream().map(item -> {
            var product = merchantClient.verifyProduct(item.productId(), item.expectedPrice());
            return OrderItem.builder()
                    .productId(product.id())
                    .productName(product.name())
                    .unitPrice(product.price())
                    .quantity(item.quantity())
                    .build();
        }).toList();
        BigDecimal itemsTotal = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = itemsTotal.add(deliveryFee);
        var availableCourier = courierClient.getAvailableCourier();
        log.info("Available courier found: {}", availableCourier.id());

        Order order = Order.builder()
                .orderNumber("ORD-" + System.currentTimeMillis())
                .customerId(request.customerId())
                .merchantId(request.merchantId())
                .courierId(availableCourier.id())
                .status(OrderStatus.ASSIGNED)
                .totalAmount(totalAmount)
                .deliveryFee(deliveryFee)
                .deliveryAddressSnapshot(address.addressText())
                .build();
        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved successfully with ID: {} and Number: {}", savedOrder.getId(), savedOrder.getOrderNumber());
        OrderStatusEvent event = new OrderStatusEvent(
                savedOrder.getId(),
                savedOrder.getCourierId(),
                "ASSIGNED"
        );

        rabbitTemplate.convertAndSend("order-exchange", "order.assigned", event);

        return orderMapper.toResponse(savedOrder);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return dist;
    }

    @Transactional
    public OrderResponse updateStatus(UUID orderId, OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        log.info("Attempting to update order {} status from {} to {}",
                orderId, order.getStatus(), nextStatus);
        if (!order.getStatus().canTransitionTo(nextStatus)) {
            throw new IllegalStatusTransitionException(
                    "Cannot transition order from " + order.getStatus() + " to " + nextStatus);
        }
        order.setStatus(nextStatus);
        Order updatedOrder = orderRepository.save(order);
        if (nextStatus == OrderStatus.DELIVERED) {
            OrderFinishedEvent event = new OrderFinishedEvent(
                    order.getId(),
                    order.getCourierId(),
                    order.getDeliveryFee(),
                    order.getTotalAmount(),
                    "DELIVERED"
            );
            rabbitTemplate.convertAndSend("order-exchange", "order.delivered", event);
            log.info("Order delivered event sent to RabbitMQ for order: {}", orderId);
        }
        if (nextStatus == OrderStatus.CANCELLED) {
            OrderStatusEvent event = new OrderStatusEvent(
                    order.getId(),
                    order.getCourierId(),
                    "CANCELLED"
            );
            rabbitTemplate.convertAndSend("order-exchange", "order.cancelled", event);
            log.info("Order {} reached terminal state: {}. Ready to release courier.", orderId, nextStatus);
        }

        return orderMapper.toResponse(updatedOrder);
    }

    public OrderResponse getOrderById(UUID id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public List<OrderResponse> getOrdersByCustomerId(UUID customerId) {
        List<Order> orders = orderRepository.findAllByCustomerIdOrderByCreatedAtDesc(customerId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getActiveOrderByCourierId(UUID courierId) {
        return orderRepository.findFirstByCourierIdAndStatusIn(
                        courierId,
                        List.of(OrderStatus.ASSIGNED, OrderStatus.PICKED_UP)
                )
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException("No active order found for this courier"));
    }

}
