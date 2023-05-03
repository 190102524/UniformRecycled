package com.uniform.ecommerce.service;

import com.uniform.ecommerce.exception.ResourceNotFoundException;
import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.repository.ProductRepository;
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

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        MultipartFile imageFile = product.getImageFile();
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
                    product.setImage("/" + "images" + "/" + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not store image file: " + fileName, e);
            }
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setStockLevel(productDetails.getStockLevel());
        product.setCategories(productDetails.getCategories());

        MultipartFile imageFile = productDetails.getImageFile();
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
                    product.setImage("/" + "images" + "/" + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not store image file: " + fileName, e);
            }
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public List<Product> search(String query) {return productRepository.findByNameContainingIgnoreCase(query);}

    // Returns list of products with stock below the specified level
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockLevelLessThan(threshold);
    }



}
