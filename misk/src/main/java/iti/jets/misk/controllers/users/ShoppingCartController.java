package iti.jets.misk.controllers.users;

import iti.jets.misk.dtos.ApiResponse;
import iti.jets.misk.dtos.CartItemDto;
import iti.jets.misk.dtos.CartResponseDto;
import iti.jets.misk.security.CustomUserDetails;
import iti.jets.misk.services.ShoppingCartService;
import iti.jets.misk.services.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponseDto>> addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        CartResponseDto cart = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cart));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        CartResponseDto cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeFromCart(
            @RequestParam Integer productId,
            Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        CartResponseDto cart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getCartCount(Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @PostMapping("/bulk-add")
    public ResponseEntity<ApiResponse<CartResponseDto>> bulkAddToCart(
            @RequestBody List<CartItemDto> items,
            Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        cartService.clearCart(userId);
        for (CartItemDto item : items) {
            if (item.getQuantity() > 0) {
                cartService.addToCart(userId, item.getProductId(), item.getQuantity());
            }
        }
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart updated successfully", cart));
    }

    private Integer getCurrentUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getUserId();
        } else {
            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
        }
    }

}