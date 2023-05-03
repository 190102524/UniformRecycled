package com.uniform.ecommerce.service;

import com.uniform.ecommerce.model.Product;
import com.uniform.ecommerce.model.Sale;
import com.uniform.ecommerce.repository.SalesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public List<Sale> getAllSales() {
        return salesRepository.findAll();
    }

    public Sale getSaleById(Integer id) {
        return salesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found with id " + id));
    }

    public Sale createSale(Sale sale) {
        Product product = sale.getProduct();
        Integer quantity = sale.getQuantity();
        BigDecimal amount = sale.getAmount();

        Sale existingSale = salesRepository.findByProduct(product);
        if (existingSale != null) {
            // Update the existing sale record with the new quantity and amount
            existingSale.setQuantity(existingSale.getQuantity() + quantity);
            existingSale.setAmount(existingSale.getAmount().add(amount));
            return salesRepository.save(existingSale);
        } else {
            // Create a new sale record
            return salesRepository.save(sale);
        }
    }

    public Sale updateSale(Integer id, Sale saleDetails) {
        Sale sale = getSaleById(id);

        sale.setProduct(saleDetails.getProduct());
        sale.setQuantity(saleDetails.getQuantity());
        sale.setAmount(saleDetails.getAmount());

        return salesRepository.save(sale);
    }

    public void deleteSale(Integer id) {
        salesRepository.deleteById(id);
    }
}
