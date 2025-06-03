package iti.jets.misk.repositories;

import iti.jets.misk.config.TestContainersConfig;
import iti.jets.misk.entities.Product;
import iti.jets.misk.entities.Shoppingcart;
import iti.jets.misk.entities.ShoppingcartId;
import iti.jets.misk.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ShoppingCartRepoTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ShoppingCartRepo shoppingCartRepo;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        // Setup test data without setting IDs manually
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.");
        user.setPhoneNumber("1234567890");
        user.setIsAdmin(false);
        testEntityManager.persistAndFlush(user);

        product = new Product();
        product.setName("Laptop");
        product.setDescription("High-end laptop");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setQuantity(10);
        product.setPhoto("laptop.jpg");
        product.setBrand("Dell");
        product.setSize("15 inch");
        product.setGender("Unisex");
        product.setIsDeleted(false);
        testEntityManager.persistAndFlush(product);
    }

    @Test
    void findByIdUserId_ShouldReturnCartItemsWhenUserExists() {
        // Arrange
        Shoppingcart cartItem = new Shoppingcart(
                new ShoppingcartId(product.getProductId(), user.getUserId()),
                user,
                product,
                2
        );
        testEntityManager.persistAndFlush(cartItem);

        // Act
        List<Shoppingcart> result = shoppingCartRepo.findByIdUserId(user.getUserId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(user.getUserId(), result.get(0).getId().getUserId());
        assertEquals(product.getProductId(), result.get(0).getId().getProductId());
        assertEquals(2, result.get(0).getQuantity());
    }

    @Test
    void findByIdUserId_ShouldReturnEmptyListWhenNoCartItems() {
        // Act
        List<Shoppingcart> result = shoppingCartRepo.findByIdUserId(user.getUserId());

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void clearCartForUser_ShouldRemoveAllItemsForUser() {
        // Arrange
        Shoppingcart cartItem1 = new Shoppingcart(
                new ShoppingcartId(product.getProductId(), user.getUserId()),
                user,
                product,
                2
        );
        testEntityManager.persistAndFlush(cartItem1);

        // Act
        shoppingCartRepo.clearCartForUser(user.getUserId());
        testEntityManager.flush();

        // Assert
        List<Shoppingcart> result = shoppingCartRepo.findByIdUserId(user.getUserId());
        assertEquals(0, result.size());
    }

    @Test
    void countByIdUserId_ShouldReturnCorrectCount() {
        // Arrange
        Shoppingcart cartItem1 = new Shoppingcart(
                new ShoppingcartId(product.getProductId(), user.getUserId()),
                user,
                product,
                2
        );
        Product product2 = new Product();
        product2.setName("Headphones");
        product2.setDescription("Wireless headphones");
        product2.setPrice(BigDecimal.valueOf(99.99));
        product2.setQuantity(5);
        product2.setPhoto("headphones.jpg");
        product2.setBrand("Sony");
        product2.setSize("Medium");
        product2.setGender("Unisex");
        product2.setIsDeleted(false);
        testEntityManager.persistAndFlush(product2);

        Shoppingcart cartItem2 = new Shoppingcart(
                new ShoppingcartId(product2.getProductId(), user.getUserId()),
                user,
                product2,
                1
        );
        testEntityManager.persistAndFlush(cartItem1);
        testEntityManager.persistAndFlush(cartItem2);

        // Act
        Long count = shoppingCartRepo.countByIdUserId(user.getUserId());

        // Assert
        assertEquals(2L, count);
    }
}