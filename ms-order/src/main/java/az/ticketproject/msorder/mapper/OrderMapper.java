package az.ticketproject.msorder.mapper;


import az.ticketproject.msorder.dto.OrderItemResponse;
import az.ticketproject.msorder.dto.OrderResponse;
import az.ticketproject.msorder.entity.Order;
import az.ticketproject.msorder.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    OrderItemResponse toItemResponse(OrderItem item);
}
