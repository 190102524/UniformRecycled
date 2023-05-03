package com.uniform.ecommerce.controller.admin;


import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.service.CategoryService;
import com.uniform.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // Returns admin product page and product list
    @GetMapping
    public ModelAndView getAdminProductPage(@RequestParam(required = false) String query) {
        ModelAndView mav = new ModelAndView("admin/product/product");
        if (query != null) {
            List<Product> products = productService.search(query);
            mav.addObject("products", products);
        } else {
            List<Product> products = productService.getAllProducts();
            mav.addObject("products", products);
        }
        return mav;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if(products.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // returns product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        if(product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    // returns page to add products
    @GetMapping("/create")
    public ModelAndView getAddProductPage() {
        ModelAndView mav = new ModelAndView("admin/product/addProduct");
        mav.addObject("product", new Product());
        mav.addObject("allCategories", categoryService.getAllCategories());
        return mav;
    }

    // creates a product and returns to product page
    @PostMapping
    public RedirectView createProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return new RedirectView("/admin/product");
    }

    // returns a ModelAndView of the update product page
    @GetMapping("/edit/{id}")
    public ModelAndView getUpdateProductPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/product/editProduct");
        Product product = productService.getProductById(id);
        mav.addObject("product", product);
        mav.addObject("allCategories", categoryService.getAllCategories());
        return mav;
    }

    // method for updating a product
    @PostMapping("/edit/{id}")
    public RedirectView updateProduct(@PathVariable("id") Integer id, @ModelAttribute Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return new RedirectView("/admin/product");
    }

    // method for deleting product, and reloading page
    @DeleteMapping("/{id}")
    public RedirectView deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return new RedirectView("/admin/product");
    }

//    // method to search for products by name
//    @GetMapping("/search")
//    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam("query") String query) {
//        List<Product> products = productService.search(query);
//        if(products.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }


}