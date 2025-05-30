package iti.jets.misk.services;

import iti.jets.misk.entities.*;
import iti.jets.misk.repositories.OrderRepo;
import iti.jets.misk.repositories.ProductRepo;
import iti.jets.misk.repositories.OrderItemsRepo;
import iti.jets.misk.utils.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderItemsRepo OrderItemsRepo;

    ValidationResult validateOrder(int userId, int addressId) {
        User user = userRepo.findUserById(userId);
        Useraddress address = addressRepo.getAddressbyAddressID(addressId);
        List<ShoppingcartItem> cartItems = shoppingCartRepo.getUserShoppingCart(userId);

        //here the total price of order will be put
        BigDecimal total = BigDecimal.ZERO;

        //here the products with shortages will be listed
        List<String> shortages = new ArrayList<>();

        for (ShoppingcartItem item : cartItems) {

            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            if (item.getProduct().getQuantity() < item.getQuantity()) {
                shortages.add(item.getProduct().getName());
            }
        }

        // adding shipping fees
        total = total.add(new BigDecimal("50.00"));


        //making decision
        if (!shortages.isEmpty() && total.compareTo(user.getCreditLimit()) > 0) {
            return new ValidationResult(false,
                    "Order exceeds credit limit and these items are low: " + shortages);
        } else if (!shortages.isEmpty()) {
            return new ValidationResult(false,
                    "These products are not available in required quantity: " + shortages);
        } else if (total.compareTo(user.getCreditLimit()) > 0) {
            return new ValidationResult(false,
                    "Your order exceeds your credit limit.");
        }

        return new ValidationResult(true, user, address, cartItems, total);
    }

    @Transactional(rollbackFor = Exception.class)
    ResponseEntity confirmOrder(ValidationResult data) {

        try {
            Order order = new Order();
            order.setUser(data.getUser());
            order.setUseraddress(data.getAddress());
            order.setTotalAmount(data.getTotal());
            order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));

            //saving the order in DB
            Order savedOrder = orderRepo.save(order);

            //if it's saved successfully then it's time to save the orderItems in Db and put them in the order table as well
            if (savedOrder != null) {
                List<Orderitems> orderItems = OrderItemsRepo.addListOfOrderItems(savedOrder, data.getCartItems());
                savedOrder.setOrderitemses(Set.copyOf(orderItems));
                orderRepo.saveAndFlush(savedOrder);

            }

            //emptying the shopping cart
            shoppingCartRepo.clearUserShoppingCart2(data.getUser().getUserId());

            //reducing the user limit
            BigDecimal newLimit = data.getUser().getCreditLimit().subtract(data.getTotal());
            userRepo.updateCreditLimit2(data.getUser().getUserId(), newLimit);

            //reducing the quantities of products
            for (ShoppingcartItem item : data.getCartItems()) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() - item.getQuantity());
                ProductRepo.updateProduct2(product);
            }

            //then cart counter has to be set to zero

            return ResponseEntity.status(HttpStatus.OK).body("Order confirmed successfully");

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order failed: " + ex.getMessage());
        }
    }

    @Transactional
    public ResponseEntity placeOrder(int userId, int addressId) {

        ValidationResult result = validateOrder(userId, addressId);
        if (!result.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }

        // If validation passed, pass all the validated data to confirmation logic
        return confirmOrder(result);

    }

    public List<Order> getAllOrders(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepo.findAll(pageable).getContent();
    }




}
