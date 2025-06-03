package iti.jets.misk.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false)
    private Integer productId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "photo", length = 255)
    private String photo;

    @Column(name = "brand", nullable = false, length = 255)
    private String brand;

    @Column(name = "size", nullable = false, length = 255)
    private String size;

    @Column(name = "gender", nullable = false, length = 6)
    private String gender;

    @Column(name = "is_deleted", nullable = true)
    private Boolean isDeleted = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product" , cascade = CascadeType.ALL)
    private Set<Orderitems> orderitemses = new HashSet<Orderitems>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private Set<Shoppingcart> shoppingcarts = new HashSet<Shoppingcart>(0);

    public Product() {
    }

    public Product(int productId, String name, BigDecimal price, int quantity, String brand, String size,
            String gender) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
        this.size = size;
        this.gender = gender;
    }

    // public Product(int productId, String name, String description, BigDecimal price, int quantity, String photo,
    //         String brand, String size, String gender, Set<Orderitems> orderitemses, Set<Shoppingcart> shoppingcarts) {
    //     this.productId = productId;
    //     this.name = name;
    //     this.description = description;
    //     this.price = price;
    //     this.quantity = quantity;
    //     this.photo = photo;
    //     this.brand = brand;
    //     this.size = size;
    //     this.gender = gender;
    //     this.orderitemses = orderitemses;
    //     this.shoppingcarts = shoppingcarts;
    // }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // public Set<Orderitems> getOrderitemses() {
    //     return this.orderitemses;
    // }

    // public void setOrderitemses(Set<Orderitems> orderitemses) {
    //     this.orderitemses = orderitemses;
    // }

    // public Set<Shoppingcart> getShoppingcarts() {
    //     return this.shoppingcarts;
    // }

    // public void setShoppingcarts(Set<Shoppingcart> shoppingcarts) {
    //     this.shoppingcarts = shoppingcarts;
    // }

    public Boolean getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
