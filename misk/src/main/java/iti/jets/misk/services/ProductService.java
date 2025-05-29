package iti.jets.misk.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import iti.jets.misk.dtos.ProductCreateUpdateDto;
import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.entities.Product;
import iti.jets.misk.exceptions.ProductNotFoundException;
import iti.jets.misk.mappers.ProductMapper;
import iti.jets.misk.repositories.ProductRepo;
import iti.jets.misk.utils.ProductSpecification;

import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    // Insert
    @Transactional
    public Product createNewProduct(ProductCreateUpdateDto productCreateDto) {
        Product product = ProductMapper.toEntity(productCreateDto);
        return productRepo.save(product);
    }

    // Select
    // Filterd for user (not_deleted=false)
    public Page<ProductDto> getProductsWithFilter(ProductFilterDto dto) {
        // must isDeleted = false
        dto.setIsDeleted(false);

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        Page<Product> productsPage = productRepo.findAll(ProductSpecification.withFilters(dto), pageable);

        Page<ProductDto> dtoPages = productsPage.map(ProductMapper::toResponseDto);
        return dtoPages;
    }

    public Page<ProductDto> getProductsWithFilterِAdmin(ProductFilterDto dto) {
        // No matter the isDeleted
        // null -> return both true & false

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        Page<Product> productsPage = productRepo.findAll(ProductSpecification.withFilters(dto), pageable);

        Page<ProductDto> dtoPages = productsPage.map(ProductMapper::toResponseDto);
        return dtoPages;
    }

    public ProductDto getProductDtoById(Integer id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        ProductDto dto = ProductMapper.toResponseDto(product);
        return dto;
    }

    // Update
    
    public ProductDto updateProduct(int productId, ProductCreateUpdateDto dto) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not Founding to Update.."));

        if (dto.getName() != null)
            product.setName(dto.getName());
        if (dto.getDescription() != null)
            product.setDescription(dto.getDescription());
        if (dto.getPrice() != null)
            product.setPrice(dto.getPrice());
        if (dto.getQuantity() != null)
            product.setQuantity(dto.getQuantity());
        if (dto.getPhoto() != null)
            product.setPhoto(dto.getPhoto());
        if (dto.getBrand() != null)
            product.setBrand(dto.getBrand());
        if (dto.getSize() != null)
            product.setSize(dto.getSize());
        if (dto.getGender() != null)
            product.setGender(dto.getGender());

        product = productRepo.saveAndFlush(product);
        return ProductMapper.toResponseDto(product);

    }

    // Delete

    public void deleteProduct(Integer productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not Found to Delete.."));

        product.setIsDeleted(true);
        productRepo.save(product);

    }

}
