package iti.jets.misk.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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


import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductUserController {

    @Autowired
    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    // Users

    // Get : Products with filterization
    @GetMapping
    public Page<ProductDto> getProducts(ProductFilterDto filterDto) {
        return productService.getProductsWithFilter(filterDto);
    }


    // Get : Product Details
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        ProductDto dto = productService.getProductDtoById(id);
        return ResponseEntity.ok(dto);
        
    }
}
