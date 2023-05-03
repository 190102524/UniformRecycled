package com.uniform.ecommerce.controller;

import com.uniform.ecommerce.model.*;
import com.uniform.ecommerce.security.CustomUserDetails;
import com.uniform.ecommerce.service.OrderService;
import com.uniform.ecommerce.service.SalesService;
import com.uniform.ecommerce.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * The ShoppingCartController class handles HTTP requests related to the shopping cart feature of the application.
 * It is annotated with @RestController to indicate that it is a controller class and @RequestMapping to map
 * requests with "/shoppingcart" to this controller.
 *
 */

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private SalesService saleService;

    private BigDecimal totalPrice;

    /**
     * This method returns the user's shopping cart page after calculating the total price of all items in the cart.
     * It only allows access to authenticated users.
     * @param authentication the current user's authentication details
     * @return a ModelAndView object containing the view name, cart items, and total price
     */
    @GetMapping
    public ModelAndView getShoppingCart(Authentication authentication) {
        // Users must log in to access cart - return to login page
        if (authentication == null) {
            return new ModelAndView("redirect:/login");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<ShoppingCart> cartItems = shoppingCartService.getCartItems(userDetails.getUser());

        totalPrice = BigDecimal.ZERO; // initialize the total price variable
        for (ShoppingCart cartItem : cartItems) {
            BigDecimal productPrice = cartItem.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(cartItem.getQuantity()); // convert the Integer to a BigDecimal
            totalPrice = totalPrice.add(productPrice.multiply(quantity)); // calculate the total price
        }

        ModelAndView modelAndView = new ModelAndView("shoppingCart");
        modelAndView.addObject("cartItems", cartItems);
        modelAndView.addObject("totalPrice", totalPrice); // add the total price to the model
        return modelAndView;
    }

    /**
     * This method adds a product to the user's cart and returns a ResponseEntity object with a success message.
     * It only allows access to authenticated users.
     * @param productId the id of the product being added to the cart
     * @param quantity the quantity of the product being added to the cart
     * @param authentication the current user's authentication details
     * @return a ResponseEntity object with a success message
     */
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity, Authentication authentication) {
        // Users must log in to add to cart - return to login page
        if (authentication == null) {
            return ResponseEntity.ok("You must be logged in to make purchases.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        shoppingCartService.addToCart(userDetails.getUser(), productId, quantity);
        return ResponseEntity.ok("Item added successfully to cart.");
    }

    /**
     * This method updates the quantity of a product in the user's cart and returns a ResponseEntity object with a success message.
     * It only allows access to authenticated users.
     * @param itemId the id of the item being updated
     * @param quantity the new quantity of the item
     * @param authentication the current user's authentication details
     * @param response the HttpServletResponse object for redirecting to the cart page
     * @return a ResponseEntity object with a success message and redirect location
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable Integer itemId, @RequestParam("quantity") Integer quantity, Authentication authentication, HttpServletResponse response) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        shoppingCartService.updateCartItemQuantity(userDetails.getUser(), itemId, quantity);
        response.setHeader("Location", "/shoppingcart");
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    /**
     * This method removes a product from the user's cart and returns a ResponseEntity object with a success message.
     * It only allows access to authenticated users.
     * @param itemId the id of the item being removed
     * @param authentication the current user's authentication details
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Integer itemId, Authentication authentication, HttpServletResponse response) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        shoppingCartService.removeCartItem(userDetails.getUser(), itemId);
        response.setHeader("Location", "/shoppingcart");
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    /**
     * Handles the checkout process when the user submits the order.
     * If the user is not logged in, they will be redirected to the login page.
     * If the shopping cart is empty, the user will be redirected to the shopping cart page.
     * Calculates the total price of the items in the shopping cart and creates a new order.
     * Decreases the stock count of the purchased items and creates a new sale record for each item.
     * Removes all items from the shopping cart after a successful checkout.
     * @param authentication represents the current authentication status of the user.
     * @param redirectAttributes attributes used for redirecting the user.
     * @return A RedirectView to the shopping cart page after a successful checkout.
     */
    @PostMapping("/checkout")
    public RedirectView checkout(Authentication authentication, RedirectAttributes redirectAttributes) {
        // Users must log in to checkout - return to login page
        if (authentication == null) {
            redirectAttributes.addAttribute("message", "You must be logged in to make purchases.");
            return new RedirectView("/shoppingcart");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<ShoppingCart> cartItems = shoppingCartService.getCartItems(userDetails.getUser());

        // Check if the cart is empty
        if (cartItems.isEmpty()) {
            redirectAttributes.addAttribute("message", "Your shopping cart is empty. Please add items to your cart before checking out.");
            return new RedirectView("/shoppingcart");
        }

        totalPrice = BigDecimal.ZERO; // initialize the total price variable
        for (ShoppingCart cartItem : cartItems) {
            BigDecimal productPrice = cartItem.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(cartItem.getQuantity()); // convert the Integer to a BigDecimal
            totalPrice = totalPrice.add(productPrice.multiply(quantity)); // calculate the total price
        }

        Order order = new Order();
        order.setUserId(userDetails.getUser().getId());
        order.setName(userDetails.getFullName());
        order.setEmail(userDetails.getUser().getEmail());
        order.setTotal(totalPrice);
        order.setStatus("NEW");

        order = orderService.createOrder(order, userDetails.getFullName(), userDetails.getUser().getId());

        List<OrderItem> orderItems = new ArrayList<>();
        for (ShoppingCart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItem.setOrder(order); // set the order ID
            orderItems.add(orderItem); // add the order item to the list
            orderItem.setStatus("NEW");

            // Decrease stock count
            cartItem.getProduct().setStockLevel(cartItem.getProduct().getStockLevel()-cartItem.getQuantity());

            // Create a new sale record
            Sale sale = new Sale();
            sale.setProduct(cartItem.getProduct());
            sale.setQuantity(cartItem.getQuantity());
            sale.setAmount(cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            saleService.createSale(sale);
        }

        order.setOrderItems(orderItems); // set the order items for the order

        orderService.createOrder(order, userDetails.getFullName(), userDetails.getUser().getId());
        shoppingCartService.removeAllCartItems(userDetails.getUser().getId());

        redirectAttributes.addAttribute("message", "Checkout successful.");


        return new RedirectView("/shoppingcart");
    }



}
