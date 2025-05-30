package iti.jets.misk.repositories;

import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.Orderitems;
import iti.jets.misk.entities.OrderitemsId;
import iti.jets.misk.entities.ShoppingcartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderItemsRepo extends JpaRepository<Orderitems, OrderitemsId> {

    @Transactional
     default List<Orderitems> addListOfOrderItems(Order order, List<ShoppingcartItem> cartItems) {
        List<Orderitems> orderItems = cartItems.stream().map(cartItem -> {
            Orderitems item = new Orderitems();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            return item;
        }).collect(Collectors.toList());

        return  saveAll(orderItems);
    }



}
