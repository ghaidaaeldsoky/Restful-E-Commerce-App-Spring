package iti.jets.misk.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;


import iti.jets.misk.dtos.ProductCreateUpdateDto;
import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.entities.Product;
import iti.jets.misk.services.ProductService;

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
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateUpdateDto dto) {
        Product createdProduct = productService.createNewProduct(dto);
        return ResponseEntity.ok(createdProduct);
    }

    // Get : All Products by filter
    @GetMapping
    public Page<ProductDto> getProductsAdmin(ProductFilterDto filterDto) {
        return productService.getProductsWithFilterِAdmin(filterDto);
    }

    // Patch : Update Product
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id ,@RequestBody ProductCreateUpdateDto productUpdateDto) {
        ProductDto dto = productService.updateProduct(id, productUpdateDto);
        return ResponseEntity.ok(dto);
    } 

    // Delete : Soft Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete Succefully");
    }

}
