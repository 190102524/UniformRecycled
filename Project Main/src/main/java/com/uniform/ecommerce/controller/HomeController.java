package com.uniform.ecommerce.controller;

import com.uniform.ecommerce.model.*;
import com.uniform.ecommerce.repository.OrderRepository;
import com.uniform.ecommerce.repository.UserRepository;
import com.uniform.ecommerce.service.CategoryService;
import com.uniform.ecommerce.service.OrderService;
import com.uniform.ecommerce.service.ProductService;
import com.uniform.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Rest controller for handling home page and registration related requests.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    /**
     * Returns home page.
     * @param model Model object
     * @param principal Principal object
     * @return ModelAndView object of the home page
     */
    @GetMapping()
    public ModelAndView viewHomePage(Model model, Principal principal) {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    /**
     * Returns about us page.
     * @param model Model object
     * @param principal Principal object
     * @return ModelAndView object of the about us page
     */
    @GetMapping("/about")
    public ModelAndView viewAbout(Model model, Principal principal) {
        ModelAndView mav = new ModelAndView("about");
        return mav;
    }

    /**
     * Returns registration page.
     * @return ModelAndView object of the registration page
     */
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("signup_form");
        mav.addObject("user", new User());
        return mav;
    }

    /**
     * Returns page for registering the user.
     * @param user User object
     * @param result BindingResult object
     * @return ModelAndView of the success page or the registration page with error message
     */
    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid User user, BindingResult result) {
        ModelAndView mav;
        if (result.hasErrors()) {
            mav = new ModelAndView("signup_form");
            return mav;
        }

        if (service.emailExists(user.getEmail())) {
            mav = new ModelAndView("signup_form");
            mav.addObject("errorMessage", "Email already exists!");
            return mav;
        }

        service.saveWithDefaultRole(user);
        mav = new ModelAndView("register_success");
        return mav;
    }


    /**
     * Returns the login page.
     *
     * @return ModelAndView representing the login page
     */
    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    /**
     * Returns the profile page for the currently logged in user.
     *
     * @param principal the principal object representing the currently logged in user
     * @return ModelAndView representing the profile page
     */
    @GetMapping("/profile")
    public ModelAndView editCurrentUser(Principal principal) {
        ModelAndView mav = new ModelAndView("user/Profilepage");
        String email = principal.getName();
        User currentUser = userRepo.findByEmail(email);
        mav.addObject("user", currentUser);
        return mav;
    }

    /**
     * Handles POST requests for editing the user's profile.
     *
     * @param user      the User object containing the edited user information
     * @param principal the principal object representing the currently logged in user
     * @return ModelAndView representing the updated profile page
     */
    @PostMapping("/edit-profile")
    public ModelAndView saveCurrentUser(@ModelAttribute("user") User user, Principal principal) {
        ModelAndView mav = new ModelAndView("user/Profilepage");
        String email = principal.getName();
        User currentUser = userRepo.findByEmail(email);
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        service.save(currentUser);
        return mav;
    }

    /**
     * Returns the orders page for the currently logged in user.
     *
     * @param principal the principal object representing the currently logged in user
     * @return ModelAndView representing the user orders page
     */
    @GetMapping("/profile/orders")
    public ModelAndView showOrders(Principal principal) {
        ModelAndView mav = new ModelAndView("user/userOrders");
        String email = principal.getName();
        User currentUser = userRepo.findByEmail(email);
        List<Order> orders = orderRepo.findByUserId(currentUser.getId());
        mav.addObject("userOrders", orders);
        return mav;
    }

    /**
     * This method cancels an order based on the provided order ID and updates its status
     * @param orderId An integer representing the ID of the order to be cancelled
     * @param status A string representing the status to be updated to
     * @param response An HttpServletResponse object to redirect to the orders page
     * @return A ResponseEntity object that returns the updated order with a HTTP status code
     * @throws IOException if there is an I/O error while redirecting the response
     */
    @PutMapping("/profile/orders/{order_id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable ("order_id") Integer orderId, @RequestParam String status, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setStatus(status);
        orderService.updateOrder(orderId, order);
        response.sendRedirect("/profile/orders");
        return null;
    }

    /**
     * This method displays further details of an order based on the provided order ID
     * @param orderId An integer representing the ID of the order
     * @return A ModelAndView object that displays the order details
     */
    @GetMapping("/profile/orders/orderdetail/{orderId}")
    public ModelAndView showOrderPage(@PathVariable Integer orderId) {
        ModelAndView mav = new ModelAndView("user/userOrderDetails");
        Order order = orderService.getOrderById(orderId);
        mav.addObject("order", order);
        return mav;
    }

    /**

     * This method cancels an individual item in an order based on the provided order ID and item ID, and updates its status
     * @param orderId An integer representing the ID of the order containing the item to be cancelled
     * @param itemId An integer representing the ID of the item to be cancelled
     * @param status A string representing the status to be updated to
     * @param response An HttpServletResponse object to redirect to the order details page
     * @return A ResponseEntity object that returns the updated order item with a HTTP status code
     * @throws IOException if there is an I/O error while redirecting the response
     */
    @PutMapping("/profile/orders/orderdetail/{orderId}/{itemId}")
    public ResponseEntity<?> cancelItem(@PathVariable Integer orderId, @PathVariable Integer itemId, @RequestParam String status, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrderItem orderItem = null;
        for (OrderItem item: order.getOrderItems()) {
            if (item.getId() == itemId){
                orderItem = item;
            }
        }

        if (orderItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderItem.setStatus(status);
        orderService.updateOrderItem(orderId, order, itemId); // pass in the itemId to update the specific OrderItem
        response.sendRedirect("/profile/orders/orderdetail/" + orderId);
        return null;
    }

    /**
     * This method loads categories and products onto homepage
     * @param id An integer representing the ID of the category
     * @return A ModelAndView object that displays the product page with the category and its products
     */
    @GetMapping("/categoryproducts/{id}")
    public ModelAndView getCategoryPage(@PathVariable("id") Integer id) {
        Category category = categoryService.getCategoryById(id);
        ModelAndView mav = new ModelAndView("productPage");
        mav.addObject("category", category);
        mav.addObject("products", category.getProducts());
        return mav;
    }

    /**
     * This method gets the product page based on the provided product ID
     * @param id An integer representing the ID of the product
     * @return A ModelAndView object that displays the product details
     */
    @GetMapping("/product/{id}")
    public ModelAndView getProductPage(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        ModelAndView mav = new ModelAndView("productDetail");
        mav.addObject("product", product);
        return mav;
    }

    /**
     * This method searches for products based on the provided query string
     * @param query A string representing the search query
     * @return A ModelAndView object that displays the search results page with the matching products
     */
    @GetMapping("/search")
    public ModelAndView searchProducts(@RequestParam("q") String query) {
        List<Product> products = productService.search(query);
        ModelAndView mav = new ModelAndView("results");
        mav.addObject("products", products);
        return mav;
    }



}
