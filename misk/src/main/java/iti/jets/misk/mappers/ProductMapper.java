package iti.jets.misk.mappers;

import iti.jets.misk.dtos.ProductCreateUpdateDto;
import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.entities.Product;

public class ProductMapper {

    public static Product toEntity(ProductCreateUpdateDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setPhoto(dto.getPhoto());
        product.setBrand(dto.getBrand());
        product.setSize(dto.getSize());
        product.setGender(dto.getGender());
        product.setIsDeleted(false); // default

        return product;
    }

    public static ProductDto toResponseDto(Product product) {
    ProductDto dto = new ProductDto();
    dto.setProductId(product.getProductId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setQuantity(product.getQuantity());
    dto.setPhoto(product.getPhoto());
    dto.setBrand(product.getBrand());
    dto.setSize(product.getSize());
    dto.setGender(product.getGender());
    return dto;
}
}
