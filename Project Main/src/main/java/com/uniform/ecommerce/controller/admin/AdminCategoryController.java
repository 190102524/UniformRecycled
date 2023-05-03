package com.uniform.ecommerce.controller.admin;

import com.uniform.ecommerce.model.Category;
import com.uniform.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


import java.util.List;

/**
 * This REST controller handles requests related to categories from the admin panel.
 * It provides methods to return the admin category page and category list, get all categories, get a category by ID, and return the page for creating categories.
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Returns the admin category page and category list.
     * @return a ModelAndView object containing the name of the view to be rendered and the categories to be displayed
     */
    @GetMapping
    public ModelAndView getAdminCategoryPage() {
        ModelAndView mav = new ModelAndView("admin/category/category");
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    /**
     * Returns all categories.
     * @return a ResponseEntity object containing the list of categories and the HTTP status code
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if(categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Returns a category with the specified ID.
     * @param id the ID of the category to be returned
     * @return a ResponseEntity object containing the category and the HTTP status code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Integer id) {
        Category category = categoryService.getCategoryById(id);
        if(category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Returns the page for creating categories.
     * @return a ModelAndView object containing the name of the view to be rendered and an empty category object to be bound with the form
     */
    @GetMapping("/create")
    public ModelAndView getAddCategoryPage() {
        ModelAndView mav = new ModelAndView("admin/category/addCategory");
        mav.addObject("category", new Category()); // create a new empty category object to bind with the form
        return mav;
    }

    /**
     * Creates a new category.
     * @param category the category to be created
     * @return a RedirectView object that redirects to the admin category page
     */
    @PostMapping
    public RedirectView createCategory(@ModelAttribute("category") Category category) {
        categoryService.createCategory(category);
        return new RedirectView("/admin/category");
    }

    /**
     * Returns the page for updating a category with the specified ID.
     * @param id the ID of the category to be updated
     * @return a ModelAndView object containing the name of the view to be rendered and the category to be updated
     */
    @GetMapping("/edit/{id}")
    public ModelAndView getUpdateCategoryPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/category/editCategory");
        Category category = categoryService.getCategoryById(id);
        mav.addObject("category", category);
        return mav;
    }

    /**
     * Updates a category with the specified ID.
     * @param id the ID of the category to be updated
     * @param category the category to be updated
     * @return a RedirectView object that redirects to the admin category page
     */
    @PostMapping("/edit/{id}")
    public RedirectView updateCategory(@PathVariable("id") Integer id, @ModelAttribute("category") Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return new RedirectView("/admin/category");
    }

    /**
     * Deletes a category with the specified ID.
     * @param id the ID of the category to be deleted
     * @return a RedirectView object that redirects to the admin category page
     */
    @DeleteMapping("/{id}")
    public RedirectView deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return new RedirectView("/admin/category");
    }
}