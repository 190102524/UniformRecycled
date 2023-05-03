package com.uniform.ecommerce.controller;

import com.uniform.ecommerce.model.Order;
import com.uniform.ecommerce.model.OrderItem;
import com.uniform.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

/**
 * The OrderController class is responsible for handling HTTP requests related to orders.
 * It is annotated with @RestController, which indicates that it is a controller class
 * that handles RESTful requests and responses.
 *
 * The @RequestMapping annotation is used to specify the base URL for all requests
 * handled by this controller.
 */
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Returns the order page based on order status - filtering.
     * @param status The order status used to filter orders. Optional parameter.
     * @return A ModelAndView object containing the view name and a list of orders
     * based on the specified order status.
     */
    @GetMapping()
    public ModelAndView ordersPage(@RequestParam(name = "status", required = false) String status) {
        ModelAndView mav = new ModelAndView("admin/order/orders");
        List<Order> orders;
        if ("new".equals(status)) {
            orders = orderService.getOrdersByStatus("NEW");
        } else if ("cancelled".equals(status)) {
            orders = orderService.getOrdersByStatus("CANCELLED");
        } else if ("accepted".equals(status)) {
            orders = orderService.getOrdersByStatus("ACCEPTED");
        } else if ("shipped".equals(status)) {
            orders = orderService.getOrdersByStatus("SHIPPED");
        } else if ("completed".equals(status)) {
            orders = orderService.getOrdersByStatus("COMPLETED");
        } else {
            orders = orderService.getAllOrders();
        }
        mav.addObject("orders", orders);
        return mav;
    }

    /**
     * Returns the order details for a given order ID.
     * @param orderId The ID of the order to retrieve.
     * @return A ModelAndView object containing the view name and the order details
     * for the specified order ID.
     */
    @GetMapping("/show/{orderId}")
    public ModelAndView showOrder(@PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);
        ModelAndView mav = new ModelAndView("admin/order/orderDetails");
        mav.addObject("order", order);
        return mav;
    }

    /**
     * Creates a new order with the specified details.
     * @param order The order to create.
     * @param name The name of the user placing the order.
     * @param userId The ID of the user placing the order.
     * @return A ResponseEntity object containing the created order and a HTTP status code
     * indicating success or failure.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order, @RequestParam String name, @RequestParam Integer userId) {
        Order createdOrder = orderService.createOrder(order, name, userId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Updates the status of an existing order.
     * @param orderId the ID of the order to be updated
     * @param status the new status to be set for the order
     * @param response the HTTP servlet response object
     * @return a ResponseEntity object representing the updated order
     * @throws IOException if there is an error while redirecting the response
     */
    @PutMapping("/{order_id}")
    public ResponseEntity<Order> updateOrder(@PathVariable ("order_id") Integer orderId, @RequestParam String status, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setStatus(status);
        orderService.updateOrder(orderId, order);
        response.sendRedirect("/admin/order");
        return null;
    }

    /**
     * Updates the status of an existing order and redirects to the order details page.
     * @param orderId the ID of the order to be updated
     * @param status the new status to be set for the order
     * @param response the HTTP servlet response object
     * @return a ResponseEntity object representing the updated order
     * @throws IOException if there is an error while redirecting the response
     */
    @PutMapping("/show/{order_id}")
    public ResponseEntity<Order> updateShowOrder(@PathVariable ("order_id") Integer orderId, @RequestParam String status, HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setStatus(status);
        orderService.updateOrder(orderId, order);
        response.sendRedirect("/admin/order/show/" + orderId);
        return null;
    }

    /**
     * Updates the status of a specific item within an order and redirects to the order details page.
     * @param orderId the ID of the order containing the item to be updated
     * @param itemId the ID of the item to be updated
     * @param status the new status to be set for the item
     * @param response the HTTP servlet response object
     * @return a ResponseEntity object representing the updated order item
     * @throws IOException if there is an error while redirecting the response
     */
    @PutMapping("/show/{orderId}/{itemId}")
    public ResponseEntity<?> updateOrderItemStatus(@PathVariable Integer orderId, @PathVariable Integer itemId, @RequestParam String status, HttpServletResponse response) throws IOException {
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
        response.sendRedirect("/admin/order/show/" + orderId);
        return null;
    }

}
