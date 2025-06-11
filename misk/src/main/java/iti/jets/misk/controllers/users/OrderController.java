package iti.jets.misk.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import iti.jets.misk.dtos.*;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.UserAlreadyExistException;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.repositories.UserRepository;
import iti.jets.misk.services.EmailService;
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
@Tag(name = "Orders")
@RestController
public class OrderController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrderService orderService;

     //admin/orders?page=1&size=10
     @Operation(summary = "Fetch orders with pagination")
     @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin/orders")
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrders(@RequestParam int page, @RequestParam  int size) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders(page,size)));
    }

    //orders?addressId=1
    @Operation(summary = "Confirm order with address ID")
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
        emailService.sendSimpleEmail(
                 userRepository.findById(userId).orElse(null).getEmail(),
                "Order Confirmation",
                "Thank you for shopping with MISK – we're thrilled to have you as part of our fragrance-loving community!\n" +
                        "\n" +
                        "We're pleased to confirm that your order has been successfully placed and is now being prepared for shipment. You can expect your order to be delivered within 3 days.\n" +
                        "\n" +
                        "Please keep an eye on your email for updates — we’ll notify you with the exact shipping time and tracking details once your order is dispatched.\n" +
                        "\n" +
                        "If you have any questions or need support, feel free to reply to this email or reach out through our website.\n" +
                        "\n" +
                        "Thank you for choosing MISK. We can't wait for you to experience our scents!\n"
        );
        return new ResponseEntity<>(ApiResponse.success(message), HttpStatus.CREATED);
    }


}
