package iti.jets.misk.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;

    public ProductService(ProductRepo productRepo, ImageService imageService) {
        this.productRepo = productRepo;
        this.imageService = imageService;
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

    // Product With uploading image:
    public ProductDto createProductWithImage(ProductCreateUpdateDto productDto, MultipartFile image)
            throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageName = imageService.uploadImage(image);
            productDto.setPhoto(imageName);
        }
        Product product = ProductMapper.toEntity(productDto);
        product = productRepo.save(product);
        return ProductMapper.toResponseDto(product);
    }

    public ProductDto updateProductWithImage(Integer id, ProductCreateUpdateDto productUpdateDto, MultipartFile photo) {

        if (photo != null && !photo.isEmpty()) {
            try {
                String img = imageService.uploadImage(photo);
                productUpdateDto.setPhoto(img);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("Failed to save product image", e);
            }
        }

        return updateProduct(id, productUpdateDto);

    }

}
