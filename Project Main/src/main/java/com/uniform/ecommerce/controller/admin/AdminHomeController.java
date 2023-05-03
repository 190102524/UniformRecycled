package com.uniform.ecommerce.controller.admin;

import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private ProductService productService;

    // Returns admin home page
    @GetMapping
    public ModelAndView getAdminHomePage() {
        ModelAndView mav = new ModelAndView("admin/index");
        List<Product> products = productService.getLowStockProducts(15);
        if(products.isEmpty()){
            return new ModelAndView("admin/index");
        }
        mav.addObject("products", products);
        return mav;
    }

    // Updates stockLevel of a product
    @PutMapping("/{productId}/replenish")
    public ResponseEntity<Product> replenishProduct(@PathVariable("productId") Integer productId, HttpServletResponse response) throws IOException {
        Product product = productService.getProductById(productId);
        product.setStockLevel(product.getStockLevel() + 100);
        Product updatedProduct = productService.updateProduct(productId, product);
        response.sendRedirect("/admin");
        return null;
    }


}