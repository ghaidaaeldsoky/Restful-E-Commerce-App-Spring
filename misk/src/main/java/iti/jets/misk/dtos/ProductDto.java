package iti.jets.misk.dtos;

import java.math.BigDecimal;

public class ProductDto {
    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String photo;
    private String brand;
    private String size;
    private String gender;

    public String getBrand() {
        return brand;
    }
    public String getDescription() {
        return description;
    }
    public String getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }
    public String getPhoto() {
        return photo;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public Integer getProductId() {
        return productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public String getSize() {
        return size;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public void setSize(String size) {
        this.size = size;
    }
}

