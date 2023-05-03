package com.uniform.ecommerce.repository;

import com.uniform.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String query);

    List<Product> findByStockLevelLessThan(Integer stockLevel);

}
