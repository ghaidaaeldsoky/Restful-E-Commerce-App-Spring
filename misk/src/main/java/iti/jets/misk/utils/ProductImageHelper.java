package iti.jets.misk.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import iti.jets.misk.dtos.ProductDto;
import jakarta.servlet.http.HttpServletRequest;

public class ProductImageHelper {
    
    // Helper method to get base URL from request
    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" +
               request.getServerName() +
               (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());
    }

    // Add full photo URL to a ProductDto
    public static ProductDto addPhotoUrl(ProductDto dto, String baseUrl) {
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            dto.setPhoto(baseUrl + "/images/" + dto.getPhoto());
        }
        return dto;
    }

    // Add full photo URL to list of ProductDto
    public static Page<ProductDto> addPhotoUrlToPage(Page<ProductDto> page, String baseUrl) {
        List<ProductDto> dtosWithFullUrl = page.getContent().stream()
            .map(dto -> addPhotoUrl(dto, baseUrl))
            .collect(Collectors.toList());
        return new PageImpl<>(dtosWithFullUrl, page.getPageable(), page.getTotalElements());
    }

}
