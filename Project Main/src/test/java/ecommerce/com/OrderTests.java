package ecommerce.com;

import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.model.*;
import com.uniform.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = EcommerceApplication.class)
public class OrderTests {


    @Autowired
    private OrderService orderService;

    private Order order;

    // Create a new test order for each test
    @BeforeEach
    void setUp() {
        order = new Order();
        order.setName("Test Order");
        order.setEmail("test@test.com");
        order.setTotal(BigDecimal.valueOf(100.00));
        order.setStatus("NEW");
        order.setUserId(1);
        order = orderService.createOrder(order, "Test User", 1);
    }

    // Test to retrieve orders by id
    @Test
    public void testGetOrderById() {
        Order retrievedOrder = orderService.getOrderById(order.getOrderId());

        // asserting order exists
        assertNotNull(retrievedOrder);
        assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
        assertEquals(order.getName(), retrievedOrder.getName());
        assertEquals(order.getEmail(), retrievedOrder.getEmail());
        assertEquals(order.getTotal(), retrievedOrder.getTotal());
        assertEquals(order.getStatus(), retrievedOrder.getStatus());
        assertEquals(order.getUserId(), retrievedOrder.getUserId());
    }

    // Retrieving all orders
    @Test
    public void testGetAllOrders() {
        Order order2 = new Order();
        order2.setName("Test Order 2");
        order2.setEmail("test2@test.com");
        order2.setTotal(new BigDecimal("20.00"));
        order2.setStatus("PROCESSING");
        order2.setUserId(1);

        orderService.createOrder(order, order.getName(), order.getUserId());
        orderService.createOrder(order2, order2.getName(), order2.getUserId());

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    // updating order details
    @Test
    void testUpdateOrder() {
        // Update the test order
        order.setName("Updated Test Order");
        order.setEmail("updated@test.com");
        order.setTotal(BigDecimal.valueOf(200.00));
        order.setStatus("IN_PROGRESS");
        Order updatedOrder = orderService.updateOrder(order.getOrderId(), order);

        // Verify that the updated order matches the original order
        assertNotNull(updatedOrder);
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getName(), updatedOrder.getName());
        assertEquals(order.getEmail(), updatedOrder.getEmail());
        assertEquals(order.getTotal(), updatedOrder.getTotal());
        assertEquals(order.getStatus(), updatedOrder.getStatus());
        assertEquals(order.getUserId(), updatedOrder.getUserId());
    }

    // testing if each item within order can be updated
    @Test
    void testUpdateOrderItem() {
        // Add an item to the test order
        OrderItem newItem = new OrderItem();
        newItem.setQuantity(2);
        newItem.setPrice(BigDecimal.valueOf(50.00));
        newItem.setProduct(new Product());
        order.getOrderItems().add(newItem);
        order = orderService.updateOrder(order.getOrderId(), order);

        // Update the item in the test order
        OrderItem updatedItem = order.getOrderItems().get(0);
        updatedItem.setQuantity(3);
        updatedItem.setPrice(BigDecimal.valueOf(75.00));
        updatedItem.setProduct(new Product());
        Order updatedOrder = orderService.updateOrderItem(order.getOrderId(), order, updatedItem.getId());

        // Verify that the updated order item matches the original order item
        assertNotNull(updatedOrder);
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getName(), updatedOrder.getName());
        assertEquals(order.getEmail(), updatedOrder.getEmail());
        assertEquals(order.getTotal(), updatedOrder.getTotal());
        assertEquals(order.getStatus(), updatedOrder.getStatus());
        assertEquals(order.getUserId(), updatedOrder.getUserId());
        assertEquals(order.getOrderItems().get(0).getId(), updatedOrder.getOrderItems().get(0).getId());
        assertEquals(order.getOrderItems().get(0).getQuantity(), updatedOrder.getOrderItems().get(0).getQuantity());
        assertEquals(order.getOrderItems().get(0).getPrice(), updatedOrder.getOrderItems().get(0).getPrice());
        assertEquals(order.getOrderItems().get(0).getProduct(), updatedOrder.getOrderItems().get(0).getProduct());
    }



}
