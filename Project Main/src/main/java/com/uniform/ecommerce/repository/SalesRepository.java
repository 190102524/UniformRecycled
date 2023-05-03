package com.uniform.ecommerce.repository;

import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Integer> {
    Sale findByProduct(Product product);
}
