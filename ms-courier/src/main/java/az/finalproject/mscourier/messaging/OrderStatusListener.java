package az.finalproject.mscourier.messaging;

import az.finalproject.mscourier.enums.CourierStatus;
import az.finalproject.mscourier.event.OrderStatusEvent;
import az.finalproject.mscourier.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusListener {

    private final CourierService courierService;

    @RabbitListener(queues = "courier-service-queue")
    public void handleOrderStatusEvent(OrderStatusEvent event) {
        log.info("Received event for Order: {} - Status: {}", event.orderId(), event.status());

        if ("ASSIGNED".equals(event.status())) {
            courierService.updateStatus(event.courierId(), CourierStatus.BUSY);
            log.info("Courier {} is now BUSY", event.courierId());
        } else if ("DELIVERED".equals(event.status())) {
            courierService.updateStatus(event.courierId(), CourierStatus.FREE);
            log.info("Courier {} is now FREE", event.courierId());
        }
    }
}
