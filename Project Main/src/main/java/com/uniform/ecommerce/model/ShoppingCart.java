package com.uniform.ecommerce.model;


import jakarta.persistence.*;

/**
 * The Entity class represents a user's shopping cart.
 * It is mapped to the "shopping_carts" table in the database.
 */
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @JoinColumn(name = "user_id")
    private Integer userId;


    public ShoppingCart() {}

    public ShoppingCart(Integer userId, Product product, Integer quantity) {
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }

    // getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

