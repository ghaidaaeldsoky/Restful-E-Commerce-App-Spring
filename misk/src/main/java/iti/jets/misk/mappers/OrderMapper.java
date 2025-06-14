package iti.jets.misk.mappers;

import iti.jets.misk.dtos.OrderDto;
import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.Orderitems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "userName", source = "order.user.name")
    @Mapping(target = "totalPrice", source = "order.totalAmount")
    @Mapping(target = "products", expression = "java(mapOrderItemsToMap(order.getOrderitemses()))")

    OrderDto toDto(Order order);
    List<OrderDto> toDtoList(List<Order> orders);

    default Map<String, Integer> mapOrderItemsToMap(Set<Orderitems> orderitemses) {
        if (orderitemses == null) return new HashMap<>();
        return orderitemses.stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getName(),
                        Orderitems::getQuantity
                ));
    }
}
