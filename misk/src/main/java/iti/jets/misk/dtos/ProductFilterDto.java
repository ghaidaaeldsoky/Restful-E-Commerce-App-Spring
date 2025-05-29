package iti.jets.misk.dtos;


import java.math.BigDecimal;
import java.util.List;

public class ProductFilterDto {
    private String name; // search by name
    private String gender;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> brands;

    private int page = 0;  // default page
    private int size = 4; // default size

    private Boolean isDeleted; // null = don't filter by deletion, true = only deleted, false = only active

    public List<String> getBrands() {
        return brands;
    }
    public String getGender() {
        return gender;
    }
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    public String getName() {
        return name;
    }
    public int getPage() {
        return page;
    }
    public int getSize() {
        return size;
    }
    public void setBrands(List<String> brands) {
        this.brands = brands;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

