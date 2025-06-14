package iti.jets.misk.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto {

    int orderId;
    String userName;
    BigDecimal totalPrice;
    Map<String,Integer> products;





    public void toStrings() {
        System.out.println(orderId + " " + userName + " " + totalPrice);
        if (products != null) {
            products.forEach((product, quantity) ->
                System.out.println("Product: " + product + ", Quantity: " + quantity));
        } else {
            System.out.println("No products in order.");
        }

    }
}


