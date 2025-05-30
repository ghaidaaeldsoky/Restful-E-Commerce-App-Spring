package iti.jets.misk.repositories;

import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer>{


    List<Order> findOrderByOrderId(int orderId);
}

