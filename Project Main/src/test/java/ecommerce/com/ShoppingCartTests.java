package ecommerce.com;

import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.model.*;
import com.uniform.ecommerce.repository.ProductRepository;
import com.uniform.ecommerce.repository.RoleRepository;
import com.uniform.ecommerce.repository.ShoppingCartRepository;
import com.uniform.ecommerce.repository.UserRepository;
import com.uniform.ecommerce.service.ProductService;
import com.uniform.ecommerce.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@SpringBootTest(classes = EcommerceApplication.class)
public class ShoppingCartTests {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepo;

    // Test to add to cart
    @Test
    public void testAddToCart() {
        User user = userRepo.getById(1);

        Product product = productRepository.getById(1);

        shoppingCartService.addToCart(user, product.getId(), 2);

        ShoppingCart cartItem = shoppingCartRepository.findByUserIdAndProductId(user.getId(), product.getId());

        assertNotNull(cartItem);
        assertEquals(user.getId(), cartItem.getUser());
        assertEquals(product.getId(), cartItem.getProduct().getId());
        assertEquals(2, cartItem.getQuantity());
    }


}
