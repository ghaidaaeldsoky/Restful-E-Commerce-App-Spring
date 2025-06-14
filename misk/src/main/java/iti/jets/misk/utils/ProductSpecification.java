package iti.jets.misk.utils;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import iti.jets.misk.dtos.ProductFilterDto;
import iti.jets.misk.entities.Product;


public class ProductSpecification {
    public static Specification<Product> withFilters(ProductFilterDto dto) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto.getName() != null && !dto.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));
            }

            if (dto.getGender() != null && !dto.getGender().isBlank()) {
                predicates.add(cb.equal(root.get("gender"), dto.getGender()));
            }

            if (dto.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), dto.getMinPrice()));
            }

            if (dto.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), dto.getMaxPrice()));
            }

            if (dto.getBrands() != null && !dto.getBrands().isEmpty()) {
                predicates.add(root.get("brand").in(dto.getBrands()));
            }

            if (dto.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), dto.getIsDeleted()));
            } 
            // else {
            //     // Default for normal users — exclude deleted
            //     predicates.add(cb.isFalse(root.get("isDeleted")));
            // }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}
