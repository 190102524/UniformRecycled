package com.uniform.ecommerce.controller.admin;

import com.uniform.ecommerce.model.Sale;
import com.uniform.ecommerce.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * The AdminSaleController class handles HTTP requests related to the Sales entity, with admin authorisation.
 */
@RestController
@RequestMapping("/admin/sale")
public class AdminSaleController {

    @Autowired
    private SalesService salesService;

    /**
     * Returns a ModelAndView object that represents the admin sale page and includes a list of all sales.
     * @return a ModelAndView object representing the admin sale page
     */
    @GetMapping
    public ModelAndView getAdminSalePage() {
        ModelAndView mav = new ModelAndView("admin/sale/sale");
        List<Sale> sales = salesService.getAllSales();
        mav.addObject("sales", sales);
        return mav;
    }

    /**
     * Returns the Sale object with the specified ID.
     * @param id the ID of the Sale object to retrieve
     * @return the Sale object with the specified ID
     */
    @GetMapping("/{id}")
    public Sale getSaleById(@PathVariable Integer id) {
        return salesService.getSaleById(id);
    }

    /**
     * Creates a new Sale object.
     * @param sale the Sale object to create
     * @return the created Sale object
     */
    @PostMapping("")
    public Sale createSale(@RequestBody Sale sale) {
        return salesService.createSale(sale);
    }

    /**
     * Updates the Sale object with the specified ID.
     * @param id the ID of the Sale object to update
     * @param sale the updated Sale object
     * @return the updated Sale object
     */
    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Integer id, @RequestBody Sale sale) {
        return salesService.updateSale(id, sale);
    }

    /**
     * Deletes the Sale object with the specified ID.
     * @param id the ID of the Sale object to delete
     * @return a ResponseEntity with an empty body and HTTP status code 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable Integer id) {
        salesService.deleteSale(id);
        return ResponseEntity.ok().build();
    }
}

