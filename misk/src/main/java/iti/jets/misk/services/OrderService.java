package iti.jets.misk.services;

import iti.jets.misk.dtos.OrderDto;
import iti.jets.misk.entities.*;
import iti.jets.misk.exceptions.*;
import iti.jets.misk.mappers.OrderMapper;
import iti.jets.misk.repositories.*;
import iti.jets.misk.utils.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderItemsRepo OrderItemsRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    ShoppingCartRepo shoppingCartRepo;

    @Autowired
    OrderMapper orderMapper;

    @Transactional
   public ValidationResult validateOrder( int userId,int addressId) {
      try {

          User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found during order validation"));
          Useraddress address = addressRepo.findById(addressId).orElseThrow(() -> new AddressNotFoundException("Address not found during order validation"));


          //check if the address is wrong somehow
          if (address.getUser().getUserId() != userId) {
              return new ValidationResult(false, "Address does not belong to the user.");
          }


          List<Shoppingcart> cartItems = shoppingCartRepo.findByIdUserId(userId);
          //check if the cart is empty
          if (cartItems.isEmpty()) {
              return new ValidationResult(false, "Your shopping cart is empty.");
          }

          //here the total price of order will be put
          BigDecimal total = BigDecimal.ZERO;

          //here the products with shortages will be listed
          List<String> shortages = new ArrayList<>();

          for (Shoppingcart item : cartItems) {

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
        catch (UserNotFoundException | AddressNotFoundException e) {
            return new ValidationResult(false, e.getMessage());
        }
        catch (Exception e) {
            return new ValidationResult(false, "An error occurred during order validation: " );
        }
    }


    @CacheEvict(value = "orders",allEntries = true )
    @Transactional(rollbackFor = Exception.class)
   public String confirmOrder(ValidationResult data) throws OrderConfirmationException {

            try{
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
                    savedOrder.setOrderitemses(new HashSet<>(orderItems));
                    orderRepo.saveAndFlush(savedOrder);
                }
                else{
                    throw new OrderConfirmationException("Order could not be saved in DB");
                }



                //emptying the shopping cart
                shoppingCartRepo.clearCartForUser(data.getUser().getUserId());
                if(shoppingCartRepo.countByIdUserId(data.getUser().getUserId())!=0){
                    throw new OrderConfirmationException("Shopping cart could not be cleared while confirmation");
                }


                //reducing the user limit
                BigDecimal oldLimit= data.getUser().getCreditLimit();
                BigDecimal newLimit = oldLimit.subtract(data.getTotal());
                User user = data.getUser();
                user.setCreditLimit(newLimit);
                if(user.getCreditLimit().compareTo(newLimit)==0){
                    throw new OrderConfirmationException("User credit limit could not be updated while  confirmation");
                }

                //reducing the quantities of products
                for (Shoppingcart item : data.getCartItems()) {
                    Product product = item.getProduct();
                    int oldQQuantity = product.getQuantity();
                    product.setQuantity(oldQQuantity - item.getQuantity());
                    if (product.getQuantity() ==oldQQuantity) {
                        throw new OrderConfirmationException("Product quantity could not be updated while confirmation");
                    }
                }

                //then cart counter has to be set to zero

                return "Order confirmed successfully";

            }
            catch (OrderConfirmationException e){
                throw e;
            }
            catch (Exception e){
                throw new OrderConfirmationException("An error occurred while confirming the order");
            }

    }


    @Transactional
    public String placeOrder(int addressId) {

        int userId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        ValidationResult result = validateOrder(userId, addressId);
        if (!result.isValid()) {

            return result.getMessage();
        }

        // If validation passed, pass all the validated data to confirmation logic

        return confirmOrder(result);
    }


    @Cacheable(value = "orders", key = "#pageNumber + '-' + #pageSize")
    @Transactional
    public Page<OrderDto> getAllOrders(int pageNumber, int pageSize) {
        try {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            var result = orderRepo.findAll(pageable);

            List<OrderDto> dtoList = orderMapper.toDtoList(result.getContent());
            return new PageImpl<OrderDto>(dtoList, pageable, result.getTotalElements());
        }
        catch (Exception e) {
            throw new GeneralErrorException("An error occurred while fetching orders: " );
        }

    }

    public Order getCertainOrder(int OrderId) {
        return orderRepo.findOrderByOrderId(OrderId).stream().findFirst().orElse(null);
    }


}
