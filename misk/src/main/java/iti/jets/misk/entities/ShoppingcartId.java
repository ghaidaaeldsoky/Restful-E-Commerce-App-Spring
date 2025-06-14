package iti.jets.misk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShoppingcartId implements java.io.Serializable {

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    public ShoppingcartId() {
    }

    public ShoppingcartId(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof ShoppingcartId))
            return false;
        ShoppingcartId castOther = (ShoppingcartId) other;

        return (this.getProductId() == castOther.getProductId())
                && (this.getUserId() == castOther.getUserId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getProductId();
        result = 37 * result + this.getUserId();
        return result;
    }

}
