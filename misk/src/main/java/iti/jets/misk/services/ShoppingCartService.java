package iti.jets.misk.services;

import iti.jets.misk.dtos.CartItemDto;
import iti.jets.misk.dtos.CartResponseDto;
import iti.jets.misk.entities.Product;
import iti.jets.misk.entities.Shoppingcart;
import iti.jets.misk.entities.ShoppingcartId;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.CartException;
import iti.jets.misk.mappers.CartItemMapper;
import iti.jets.misk.repositories.ProductRepo;
import iti.jets.misk.repositories.ShoppingCartRepo;
import iti.jets.misk.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepo cartRepository;
    private final ProductRepo productRepository;
    private final UserRepository userRepository;

    public ShoppingCartService(ShoppingCartRepo cartRepository, ProductRepo productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartResponseDto addToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CartException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CartException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new CartException("Insufficient stock. Available quantity is : " + product.getQuantity());
        }

        ShoppingcartId cartId = new ShoppingcartId(productId, userId);
        Shoppingcart existingItem = cartRepository.findById(cartId).orElse(null);

        if (existingItem != null) {
            if(existingItem.getQuantity() == product.getQuantity()) {
                throw new CartException("You already have the maximum quantity of this product in your cart.");
            }
            int newQuantity = Math.min(existingItem.getQuantity() + quantity, product.getQuantity());
            existingItem.setQuantity(newQuantity);
            existingItem.setAddedAt(LocalDateTime.now());
            cartRepository.save(existingItem);
        } else {
            Shoppingcart newItem = new Shoppingcart(cartId, user, product, quantity);
            cartRepository.save(newItem);
        }

        return getCartByUserId(userId);
    }

    public CartResponseDto updateCartItem(Integer userId, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }

        ShoppingcartId cartId = new ShoppingcartId(productId, userId);
        Shoppingcart cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException("Cart item not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CartException("Product not found"));

        if (quantity > product.getQuantity()) {
            throw new CartException("Insufficient stock. Available: " + product.getQuantity());
        }

        cartItem.setQuantity(quantity);
        cartItem.setAddedAt(LocalDateTime.now());
        cartRepository.save(cartItem);

        return getCartByUserId(userId);
    }

    public CartResponseDto removeFromCart(Integer userId, Integer productId) {
        ShoppingcartId cartId = new ShoppingcartId(productId, userId);
        cartRepository.deleteById(cartId);
        return getCartByUserId(userId);
    }

    public CartResponseDto getCartByUserId(Integer userId) {
        List<Shoppingcart> cartItems = cartRepository.findByIdUserId(userId);
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(CartItemMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return new CartResponseDto(cartItemDtos);
    }

    public void clearCart(Integer userId) {
        cartRepository.clearCartForUser(userId);
    }

    public Long getCartItemCount(Integer userId) {
        return cartRepository.countByIdUserId(userId);
    }

}