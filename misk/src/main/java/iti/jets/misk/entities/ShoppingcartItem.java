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
import java.sql.Timestamp;


@Entity
@Table(name = "shoppingcart", catalog = "misk2")
public class ShoppingcartItem implements java.io.Serializable {
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
    private Timestamp addedAt;

    public ShoppingcartItem() {
    }

    public ShoppingcartItem(ShoppingcartId id, User user, Product product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public ShoppingcartItem(ShoppingcartId id, User user, Product product, int quantity, Timestamp addedAt) {
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

    
    public Timestamp getAddedAt() {
        return this.addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }

}
