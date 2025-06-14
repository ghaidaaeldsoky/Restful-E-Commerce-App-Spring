
package iti.jets.misk.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import iti.jets.misk.dtos.ApiResponse;
import iti.jets.misk.dtos.CartItemDto;
import iti.jets.misk.dtos.CartResponseDto;
import iti.jets.misk.services.ShoppingCartService;
import iti.jets.misk.utils.AuthenticationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shopping Cart")
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    private final AuthenticationUtil authenticationUtil;

    public ShoppingCartController(ShoppingCartService cartService , AuthenticationUtil authenticationUtil) {
        this.cartService = cartService;
        this.authenticationUtil=authenticationUtil;
    }


    @Operation(summary = "Add item to cart using product ID and quantity")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponseDto>> addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        CartResponseDto cart = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cart));
    }

    @Operation(summary = "Update item in cart using product ID and quantity")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        CartResponseDto cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cart));
    }

    @Operation(summary = "Remove item from cart using product ID")
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeFromCart(
            @RequestParam Integer productId,
            Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        CartResponseDto cart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
    }

    @Operation(summary = "Clear the entire cart")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }

    @Operation(summary = "Get the current user's cart with all items")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @Operation(summary = "Get the count of items in the cart")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getCartCount(Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @Operation(summary = "add  multiple items to cart by providing a list of CartItems")
    @PostMapping("/bulk-add")
    public ResponseEntity<ApiResponse<CartResponseDto>> bulkAddToCart(
            @RequestBody List<CartItemDto> items,
            Authentication auth) {
        Integer userId = authenticationUtil.getCurrentUserId(auth);
        cartService.clearCart(userId);
        for (CartItemDto item : items) {
            if (item.getQuantity() > 0) {
                cartService.addToCart(userId, item.getProductId(), item.getQuantity());
            }
        }
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart updated successfully", cart));
    }

//    private Integer getCurrentUserId(Authentication auth) {
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new IllegalStateException("User is not authenticated");
//        }
//
//        Object principal = auth.getPrincipal();
//        if (principal instanceof CustomUserDetails) {
//            CustomUserDetails userDetails = (CustomUserDetails) principal;
//            return userDetails.getUserId();
//        } else {
//            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
//        }
//    }

}