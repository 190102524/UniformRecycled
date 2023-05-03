package com.uniform.ecommerce.repository;

import com.uniform.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * The OrderRepository interface provides methods for accessing order data in the database.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByStatus(String status);
    public List<Order> findByUserId(Integer userId);
}
