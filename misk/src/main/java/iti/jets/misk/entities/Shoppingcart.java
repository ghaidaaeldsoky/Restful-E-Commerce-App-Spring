package iti.jets.misk.entities;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ToString(exclude = {"user", "product"})
@Entity
@Table(name = "shoppingcart")
public class Shoppingcart implements java.io.Serializable {
    @EmbeddedId

    @AttributeOverrides({
            @AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
            @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)) })
    private ShoppingcartId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_at", length = 19)
    private LocalDateTime addedAt = LocalDateTime.now();

    public Shoppingcart() {
    }

    public Shoppingcart(ShoppingcartId id, User user, Product product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Shoppingcart(ShoppingcartId id, User user, Product product, int quantity, LocalDateTime addedAt) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }

    
    public ShoppingcartId getId() {
        return this.id;
    }

    public void setId(ShoppingcartId id) {
        this.id = id;
    }

    
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    
    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    public LocalDateTime getAddedAt() {
        return this.addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

}
