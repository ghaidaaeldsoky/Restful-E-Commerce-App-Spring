package iti.jets.misk.utils;

import iti.jets.misk.entities.Shoppingcart;
import iti.jets.misk.entities.User;
import iti.jets.misk.entities.Useraddress;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ValidationResult {
    private boolean valid;
    private String message;
    private User user;
    private Useraddress address;
    private List<Shoppingcart> cartItems;
    private BigDecimal total;

    public ValidationResult() {
    }

    // Constructor for failure
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    // Constructor for success
    public ValidationResult(boolean valid, User user, Useraddress address,
                            List<Shoppingcart> cartItems, BigDecimal total) {
        this.valid = valid;
        this.user = user;
        this.address = address;
        this.cartItems = cartItems;
        this.total = total;
    }


    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
    public Useraddress getAddress() { return address; }
    public List<Shoppingcart> getCartItems() { return cartItems; }
    public BigDecimal getTotal() { return total; }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", address=" + address +
                ", cartItems=" + cartItems +
                ", total=" + total +
                '}';
    }
}