package iti.jets.misk.controllers.users;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.PageImpl;

import iti.jets.misk.dtos.ProductDto;
import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.services.ProductService;
import iti.jets.misk.utils.ProductImageHelper;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/public/products")
public class ProductUserController {

    @Autowired
    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    // Users

    // Get : Products with filterization
    @GetMapping
    public Page<ProductDto> getProducts(ProductFilterDto filterDto, HttpServletRequest req) {
        Page<ProductDto> productPage = productService.getProductsWithFilter(filterDto);
        String baseUrl = ProductImageHelper.getBaseUrl(req);
        return ProductImageHelper.addPhotoUrlToPage(productPage,baseUrl);
    }


    // Get : Product Details
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id, HttpServletRequest req) {
        ProductDto dto = productService.getProductDtoById(id);
        String baseUrl = ProductImageHelper.getBaseUrl(req);
        System.out.println("after base url");
        dto = ProductImageHelper.addPhotoUrl(dto, baseUrl);
        return ResponseEntity.ok(dto);
        
    }


   
}
