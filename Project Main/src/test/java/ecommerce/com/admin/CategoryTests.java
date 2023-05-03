package ecommerce.com.admin;

import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.exception.ResourceNotFoundException;
import com.uniform.ecommerce.model.Category;
import com.uniform.ecommerce.repository.CategoryRepository;
import com.uniform.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

@SpringBootTest(classes = EcommerceApplication.class)
public class CategoryTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    // By default, there should be no categories.
    @Test
    public void testGetAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories);
        assertEquals(0, categories.size());
    }

    // Testing creating a category
    @Test
    public void testCreateCategory() {
        Category category = new Category();
        category.setName("TestCategory");
        category.setDescription("TestDescription");
        category.setImage("/images/test.jpg");
        Category savedCategory = categoryService.createCategory(category);
        assertNotNull(savedCategory.getId());
        assertEquals("TestCategory", savedCategory.getName());
        assertEquals("TestDescription", savedCategory.getDescription());
        assertEquals("/images/test.jpg", savedCategory.getImage());
    }

    // Testing updating a category
    @Test
    public void testUpdateCategory() {
        Category category = new Category();
        category.setName("TestCategory");
        category.setDescription("TestDescription");
        category.setImage("/images/test.jpg");
        Category savedCategory = categoryRepository.save(category);

        Category updatedCategory = new Category();
        updatedCategory.setName("UpdatedTestCategory");
        updatedCategory.setDescription("UpdatedTestDescription");
        updatedCategory.setImage("/images/updated_test.jpg");
        Category updatedSavedCategory = categoryService.updateCategory(savedCategory.getId(), updatedCategory);

        assertEquals(savedCategory.getId(), updatedSavedCategory.getId());
        assertEquals("UpdatedTestCategory", updatedSavedCategory.getName());
        assertEquals("UpdatedTestDescription", updatedSavedCategory.getDescription());
        assertEquals(category.getImage(), updatedSavedCategory.getImage());
    }

    // Testing if category can be retrieved by id
    @Test
    public void testGetCategoryById() {
        // create a category
        Category category = new Category();
        category.setName("Test category");
        category.setDescription("Test category description");
        category.setImage("/images/test.jpg");
        category = categoryService.createCategory(category);

        // retrieve the category by id
        Category retrievedCategory = categoryService.getCategoryById(category.getId());

        // verify that the retrieved category matches the original category
        assertEquals(category.getId(), retrievedCategory.getId());
        assertEquals(category.getName(), retrievedCategory.getName());
        assertEquals(category.getDescription(), retrievedCategory.getDescription());
        assertEquals(category.getImage(), retrievedCategory.getImage());
    }

    // Testing category delete function
    @Test
    public void testDeleteCategory() {
        // create a category
        Category category = new Category();
        category.setName("Test category");
        category.setDescription("Test category description");
        category.setImage("/images/test.jpg");
        category = categoryService.createCategory(category);

        // delete the category
        categoryService.deleteCategory(category.getId());

        // verify that the category was deleted
        Category finalCategory = category;
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(finalCategory.getId());
        });
    }

}
