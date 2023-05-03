package ecommerce.com.admin;

import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.model.Category;
import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = EcommerceApplication.class)
public class ProductTests {

    @Autowired
    private ProductService productService;

    // Test for creating a product
    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.00"));
        product.setDescription("This is a test product");
        product.setStockLevel(5);
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setName("Test Category");
        categories.add(category);
        product.setCategories(categories);

        Product savedProduct = productService.createProduct(product);

        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(new BigDecimal("10.00"), savedProduct.getPrice());
        assertEquals("This is a test product", savedProduct.getDescription());
        assertEquals(Integer.valueOf(5), savedProduct.getStockLevel());
        assertNotNull(savedProduct.getImage());
        assertFalse(savedProduct.getCategories().isEmpty());
        assertEquals("Test Category", savedProduct.getCategories().get(0).getName());
    }

    // retrieving products
    @Test
    public void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    // retrieving specific product
    @Test
    public void testGetProductById() {
        Product product = productService.getProductById(1);

        assertNotNull(product);
        assertEquals("Product 1", product.getName());
    }

    // updating specific product
    @Test
    public void testUpdateProduct() {
        Product product = productService.getProductById(1);
        product.setName("Updated Product");
        product.setPrice(new BigDecimal("20.00"));
        product.setDescription("This is an updated product");
        product.setStockLevel(10);

        Product updatedProduct = productService.updateProduct(1, product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(new BigDecimal("20.00"), updatedProduct.getPrice());
        assertEquals("This is an updated product", updatedProduct.getDescription());
        assertEquals(Integer.valueOf(10), updatedProduct.getStockLevel());
    }

    // deleting specific product
    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1);
        assertNull(productService.getProductById(1));
    }

    // testing search function
    @Test
    public void testSearch() {
        List<Product> products = productService.search("Product");

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }
}
