package iti.jets.misk.controllers.users;

import iti.jets.misk.dtos.*;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.UserAlreadyExistException;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.repositories.UserRepository;
import iti.jets.misk.services.JWTProvider;
import iti.jets.misk.services.OrderService;
import iti.jets.misk.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

//    @Autowired
//    EmailService emailService;

//    @Autowired
//    UserRepository userRepository;

    @Autowired
    private OrderService orderService;

     //admin/orders?page=1&size=10
     @PreAuthorize("hasAuthority('USER')")
    @GetMapping("admin/orders")
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrders(@RequestParam int page, @RequestParam  int size) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders(page,size)));
    }

    //orders?addressId=1
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<String>> confirmOrder(@RequestParam int addressId) {
        String message = orderService.placeOrder(addressId);
        if (!message.equals("Order confirmed successfully")) {
            if (message.contains("An error occurred during order validation: ")) {
                return new ResponseEntity<>(ApiResponse.error(message), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(ApiResponse.error(message), HttpStatus.NOT_FOUND);
        }
        int userId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
//        emailService.sendSimpleEmail(
//                 userRepository.findById(userId).orElse(null).getEmail(),
//                "Order Confirmation",
//                "Your order has been confirmed successfully. Thank you for shopping with us!"
//        );
        return new ResponseEntity<>(ApiResponse.success(message), HttpStatus.CREATED);
    }


}
