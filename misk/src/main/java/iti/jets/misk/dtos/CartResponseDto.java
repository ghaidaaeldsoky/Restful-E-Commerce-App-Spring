package iti.jets.misk.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartResponseDto implements Serializable {
    private List<CartItemDto> items;
    private Integer totalItems;
    private BigDecimal totalPrice;

    public CartResponseDto() {}

    public CartResponseDto(List<CartItemDto> items) {
        this.items = items;
        this.totalItems = items.size();
        this.totalPrice = items.stream()
                .map(CartItemDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

}
