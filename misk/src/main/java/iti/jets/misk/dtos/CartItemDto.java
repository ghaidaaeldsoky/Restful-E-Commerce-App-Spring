package iti.jets.misk.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemDto implements Serializable {
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private LocalDateTime addedAt;
    private String productName;
    private String productPhoto;
    private BigDecimal productPrice;
    private Integer availableStock;

    public CartItemDto() {}

    public CartItemDto(Integer userId, Integer productId, Integer quantity,
                       LocalDateTime addedAt, String productName, String productPhoto,
                       BigDecimal productPrice, Integer availableStock) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productPrice = productPrice;
        this.availableStock = availableStock;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductPhoto() { return productPhoto; }
    public void setProductPhoto(String productPhoto) { this.productPhoto = productPhoto; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public Integer getAvailableStock() { return availableStock; }
    public void setAvailableStock(Integer availableStock) { this.availableStock = availableStock; }

    public BigDecimal getTotalPrice() {
        return productPrice != null ? productPrice.multiply(BigDecimal.valueOf(quantity)) : BigDecimal.ZERO;
    }
}