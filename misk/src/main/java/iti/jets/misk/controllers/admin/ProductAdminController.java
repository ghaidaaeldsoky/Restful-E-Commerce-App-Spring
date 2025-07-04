package iti.jets.misk.controllers.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;

import iti.jets.misk.dtos.ProductCreateUpdateDto;
import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.entities.Product;
import iti.jets.misk.services.ProductService;
import iti.jets.misk.utils.ProductImageHelper;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Admin Products Endpoints")
@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {
    @Autowired
    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    // Admin

    // Post : new Product
    @Operation(summary = "Create a new product without an image")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/no")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateUpdateDto dto) {
        Product createdProduct = productService.createNewProduct(dto);
        return ResponseEntity.ok(createdProduct);
    }


    @Operation(summary = "Create a new product with an optional image")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductCreateUpdateDto dto = mapper.readValue(productJson, ProductCreateUpdateDto.class);
            ProductDto createdProduct = productService.createProductWithImage(dto, photo);
            return ResponseEntity.ok(createdProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // // Create with image:
    // @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<Product> createProduct(
    // @RequestPart("product") ProductCreateUpdateDto dto,
    // @RequestPart(value = "photo", required = false) MultipartFile photo) {
    // Product createdProduct = productService.createNewProduct(dto, photo);
    // return ResponseEntity.ok(createdProduct);
    // }

    // Get : All Products by filter
    @Operation(summary = "Get all products after applying filtration and pagination")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<ProductDto> getProductsAdmin(ProductFilterDto filterDto, HttpServletRequest req) {
        Page<ProductDto> productPage = productService.getProductsWithFilterِAdmin(filterDto);
        String baseUrl = ProductImageHelper.getBaseUrl(req);
        return ProductImageHelper.addPhotoUrlToPage(productPage, baseUrl);
    }

    // Patch : Update Product
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update product by ID without updating image")
    @PatchMapping("/no/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id,
                                                    @RequestBody ProductCreateUpdateDto productUpdateDto) {
        ProductDto dto = productService.updateProduct(id, productUpdateDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update product by ID with updating image")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProductWithImage(
            @PathVariable Integer id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductCreateUpdateDto productUpdateDto = mapper.readValue(productJson, ProductCreateUpdateDto.class);

            ProductDto updatedProduct = productService.updateProductWithImage(id, productUpdateDto, photo);
            return ResponseEntity.ok(updatedProduct);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete : Soft Delete
    @Operation(summary = "Delete product by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted Successfully");
        return ResponseEntity.ok(response);
    }

}
