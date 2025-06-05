package iti.jets.misk.repositories;

import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.Orderitems;
import iti.jets.misk.entities.OrderitemsId;
import iti.jets.misk.entities.Shoppingcart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderItemsRepo extends JpaRepository<Orderitems, OrderitemsId> {

    @Transactional
     default List<Orderitems> addListOfOrderItems(Order order, List<Shoppingcart> cartItems) {
        List<Orderitems> orderItems = cartItems.stream().map(cartItem -> {
            OrderitemsId id = new OrderitemsId();
            id.setOrderId(order.getOrderId());
            id.setProductId(cartItem.getProduct().getProductId());
            Orderitems item = new Orderitems();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setId(id);
            return item;
        }).collect(Collectors.toList());

        return  saveAll(orderItems);
    }



}
