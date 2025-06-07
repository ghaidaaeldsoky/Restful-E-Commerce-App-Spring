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

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}

