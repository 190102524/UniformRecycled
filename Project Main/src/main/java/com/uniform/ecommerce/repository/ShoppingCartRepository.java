package com.uniform.ecommerce.repository;

import com.uniform.ecommerce.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    List<ShoppingCart> findByUserId(Integer userId);
    ShoppingCart findByUserIdAndProductId(Integer userId, Integer productId);
    ShoppingCart findByUserIdAndId(Integer userId, Integer itemId);
}