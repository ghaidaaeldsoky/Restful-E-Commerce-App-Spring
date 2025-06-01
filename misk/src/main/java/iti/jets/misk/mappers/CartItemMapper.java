package iti.jets.misk.mappers;

import iti.jets.misk.dtos.CartItemDto;
import iti.jets.misk.entities.Shoppingcart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "userId", source = "cartItem.id.userId")
    @Mapping(target = "productId", source = "cartItem.id.productId")
    @Mapping(target = "quantity", expression = "java(Math.min(cartItem.getQuantity(), cartItem.getProduct().getQuantity()))")
    @Mapping(target = "addedAt", source = "cartItem.addedAt")
    @Mapping(target = "productName", source = "cartItem.product.name")
    @Mapping(target = "productPhoto", source = "cartItem.product.photo")
    @Mapping(target = "productPrice", source = "cartItem.product.price")
    @Mapping(target = "availableStock", source = "cartItem.product.quantity")
    CartItemDto toDto(Shoppingcart cartItem);
}