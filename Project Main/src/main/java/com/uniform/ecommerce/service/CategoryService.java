package com.uniform.ecommerce.service;

import com.uniform.ecommerce.exception.ResourceNotFoundException;
import com.uniform.ecommerce.model.Category;
import com.uniform.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * This class represents the service layer for Category entity, providing CRUD operations.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Retrieves a list of all categories from the database.
     * @return A list of Category objects.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a Category object from the database by its ID.
     * @param id The ID of the category to retrieve.
     * @return The Category object with the given ID.
     * @throws ResourceNotFoundException If no category with the given ID is found.
     */
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    /**
     * Creates a new category in the database.
     * @param category The Category object to be created.
     * @return The newly created Category object.
     * @throws RuntimeException If the image file associated with the category cannot be stored.
     */
    public Category createCategory(Category category) {
        MultipartFile imageFile = category.getImageFile();
        if(imageFile!= null && !imageFile.isEmpty()) {
            // Store the image file on the file system
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String uploadDir = "src/main/resources/static/images";
            Path uploadPath = Paths.get(uploadDir);
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    category.setImage("/" + "images" + "/" + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not store image file: " + fileName, e);
            }
        }

        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category in the database.
     * @param id The ID of the category to be updated.
     * @param categoryDetails The Category object containing the updated data.
     * @return The updated Category object.
     * @throws ResourceNotFoundException If no category with the given ID is found.
     * @throws RuntimeException If the new image file associated with the category cannot be stored.
     */
    public Category updateCategory(Integer id, Category categoryDetails) {
        Category category = getCategoryById(id);
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        MultipartFile imageFile = categoryDetails.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            // Store the new image file on the file system
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String uploadDir = "src/main/resources/static/images";
            Path uploadPath = Paths.get(uploadDir);
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    category.setImage("/" + "images" + "/" + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not store image file: " + fileName, e);
            }
        }

        return categoryRepository.save(category);
    }

    /**
     * Deletes a category from the database by its ID.
     * @param id The ID of the category to be deleted.
     */
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}

