package iti.jets.misk.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import iti.jets.misk.entities.Product;

public interface ProductRepo extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product>{
    
    List<Product> findTop6ByIsDeletedFalseOrderByProductIdDesc(); // for homepage

    // Optional<Product> findProductById(); 




}

