package com.uniform.ecommerce.service;

import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.model.ShoppingCart;
import com.uniform.ecommerce.model.User;
import com.uniform.ecommerce.repository.ProductRepository;
import com.uniform.ecommerce.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ShoppingCart> getCartItems(User user) {
        return shoppingCartRepository.findByUserId(user.getId());
    }

    public void addToCart(User user, Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        ShoppingCart existingItem = shoppingCartRepository.findByUserIdAndProductId(user.getId(), productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartRepository.save(existingItem);
        } else {
            ShoppingCart newItem = new ShoppingCart(user.getId(), product, quantity);
            shoppingCartRepository.save(newItem);
        }
    }

    public void updateCartItemQuantity(User user, Integer itemId, Integer quantity) {
        ShoppingCart cartItem = shoppingCartRepository.findByUserIdAndId(user.getId(), itemId);

        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            shoppingCartRepository.save(cartItem);
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    public void removeCartItem(User user, Integer itemId) {
        ShoppingCart cartItem = shoppingCartRepository.findByUserIdAndId(user.getId(), itemId);

        if (cartItem != null) {
            shoppingCartRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    // Used when order is made - deletes cart data
    public void removeAllCartItems(Integer userId) {
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUserId(userId);
        shoppingCartRepository.deleteAll(cartItems);
    }

}
