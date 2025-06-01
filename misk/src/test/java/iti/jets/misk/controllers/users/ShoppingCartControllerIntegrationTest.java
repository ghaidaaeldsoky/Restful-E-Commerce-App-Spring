package iti.jets.misk.controllers.users;

import iti.jets.misk.config.TestContainersConfig;
import iti.jets.misk.entities.Product;
import iti.jets.misk.entities.Shoppingcart;
import iti.jets.misk.entities.ShoppingcartId;
import iti.jets.misk.entities.User;
import iti.jets.misk.repositories.ProductRepo;
import iti.jets.misk.repositories.ShoppingCartRepo;
import iti.jets.misk.repositories.UserRepository;
import iti.jets.misk.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private ShoppingCartRepo shoppingCartRepository;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Clear
        shoppingCartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Save and flush user
        testUser = new User();
        testUser.setName("Test User");
        testUser.setPhoneNumber("1234567890");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setIsAdmin(false);
        testUser = userRepository.saveAndFlush(testUser);

        // Save and flush product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(10.0));
        testProduct.setQuantity(50);
        testProduct.setBrand("Test Brand");
        testProduct.setSize("M");
        testProduct.setGender("Unisex");
        testProduct.setIsDeleted(false);
        testProduct = productRepository.saveAndFlush(testProduct);

        // Now safe to insert into cart
        Shoppingcart cartItem = new Shoppingcart();
        ShoppingcartId cartId = new ShoppingcartId(testProduct.getProductId(), testUser.getUserId());
        cartItem.setId(cartId);
        cartItem.setUser(testUser);
        cartItem.setProduct(testProduct);
        cartItem.setQuantity(2);
        cartItem.setAddedAt(LocalDateTime.now());
        shoppingCartRepository.saveAndFlush(cartItem);

        // Set authentication
        CustomUserDetails customUserDetails = new CustomUserDetails(testUser);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void addToCart_Unauthenticated_ShouldRedirectToLogin() throws Exception {
        SecurityContextHolder.clearContext();
        mockMvc.perform(post("/cart/add")
                        .param("productId", "102")
                        .param("quantity", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void addToCart_Authenticated_ShouldAddItemAndReturnCart() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(BigDecimal.valueOf(20.0));
        newProduct.setQuantity(30);
        newProduct.setBrand("New Brand");
        newProduct.setSize("L");
        newProduct.setGender("Unisex");
        newProduct.setIsDeleted(false);
        newProduct = productRepository.save(newProduct);

        mockMvc.perform(post("/cart/add")
                        .param("productId", String.valueOf(newProduct.getProductId()))
                        .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Item added to cart"))
                .andExpect(jsonPath("$.data.items[0].productId").value(testProduct.getProductId()))
                .andExpect(jsonPath("$.data.items[1].productId").value(newProduct.getProductId()));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void addToCart_Authenticated_ShouldFail_WhenProductNotFound() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("productId", "999")
                        .param("quantity", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void updateCartItem_Authenticated_ShouldUpdateQuantityAndReturnCart() throws Exception {
        mockMvc.perform(put("/cart/update")
                        .param("productId", String.valueOf(testProduct.getProductId()))
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart updated"))
                .andExpect(jsonPath("$.data.items[0].quantity").value(5));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void removeFromCart_ShouldReturnUpdatedCart() throws Exception {

        mockMvc.perform(delete("/cart/remove")
                        .param("productId", String.valueOf(testProduct.getProductId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Item removed from cart"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void clearCart_Authenticated_ShouldClearCart() throws Exception {
        mockMvc.perform(delete("/cart/clear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart cleared"));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void getCart_Authenticated_ShouldReturnCartItems() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].productId").value(testProduct.getProductId()))
                .andExpect(jsonPath("$.data.items[0].quantity").value(2));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void getCartCount_Authenticated_ShouldReturnCount() throws Exception {
        mockMvc.perform(get("/cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void bulkAddToCart_Authenticated_ShouldAddMultipleItems() throws Exception {
        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.0));
        product2.setQuantity(30);
        product2.setBrand("Brand 2");
        product2.setSize("L");
        product2.setGender("Unisex");
        product2.setIsDeleted(false);
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Product 3");
        product3.setPrice(BigDecimal.valueOf(30.0));
        product3.setQuantity(20);
        product3.setBrand("Brand 3");
        product3.setSize("S");
        product3.setGender("Unisex");
        product3.setIsDeleted(false);
        product3 = productRepository.save(product3);

        String requestBody = String.format("""
            [
                {"productId": %d, "quantity": 3},
                {"productId": %d, "quantity": 1}
            ]
            """, product2.getProductId(), product3.getProductId());

        mockMvc.perform(post("/cart/bulk-add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart updated successfully"))
                .andExpect(jsonPath("$.data.items[0].productId").value(product2.getProductId()))
                .andExpect(jsonPath("$.data.items[1].productId").value(product3.getProductId()));

        mockMvc.perform(get("/cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(2));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void bulkAddToCart_Authenticated_ShouldSkipItemsWithZeroQuantity() throws Exception {
        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.0));
        product2.setQuantity(30);
        product2.setBrand("Brand 2");
        product2.setSize("L");
        product2.setGender("Unisex");
        product2.setIsDeleted(false);
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Product 3");
        product3.setPrice(BigDecimal.valueOf(30.0));
        product3.setQuantity(20);
        product3.setBrand("Brand 3");
        product3.setSize("S");
        product3.setGender("Unisex");
        product3.setIsDeleted(false);
        product3 = productRepository.save(product3);

        String requestBody = String.format("""
            [
                {"productId": %d, "quantity": 0},
                {"productId": %d, "quantity": 1}
            ]
            """, product2.getProductId(), product3.getProductId());

        mockMvc.perform(post("/cart/bulk-add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart updated successfully"))
                .andExpect(jsonPath("$.data.items[0].productId").value(product3.getProductId()));

        mockMvc.perform(get("/cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
//    @WithMockUser(username = "test@example.com", roles = "USER")
    void removeCartItem_Authenticated_ShouldRemoveItem() throws Exception {
        mockMvc.perform(delete("/cart/remove")
                        .param("productId", String.valueOf(testProduct.getProductId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Item removed from cart"));

        mockMvc.perform(get("/cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(0));
    }
}