package com.kozitskiy.order.analyzer;

import com.kozitskiy.salesandcustomeranalysis.entity.Customer;
import com.kozitskiy.salesandcustomeranalysis.entity.Order;
import com.kozitskiy.salesandcustomeranalysis.entity.OrderItem;
import com.kozitskiy.salesandcustomeranalysis.entity.OrderStatus;
import com.kozitskiy.salesandcustomeranalysis.util.OrderAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSException;

import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderAnalyzerTest {
    private Customer customer1;
    private Customer customer2;
    private OrderItem laptop;
    private OrderItem book;
    private OrderItem tShirt;

    @BeforeEach
    void setUp(){
        customer1 = Customer.builder().city("Smolensk").build();
        customer2 = Customer.builder().city("Pinsk").build();

        laptop = OrderItem.builder().price(1200).quantity(10).productName("laptop").build();
        book = OrderItem.builder().price(10).quantity(150).productName("book").build();
        tShirt = OrderItem.builder().price(10).quantity(150).productName("tShirt").build();
    }

    @Test
    void getUniqueCitiesTest(){
        Order order1 = Order.builder().orderId("001").customer(customer1).items(Collections.singletonList(laptop)).build();
        Order order2 = Order.builder().orderId("002").customer(customer2).items(List.of(book)).build();
        Order order3 = Order.builder().orderId("003").customer(customer1).items(List.of(tShirt)).build();

        List<Order> orders = List.of(order1, order2, order3);
        List<String> cities = OrderAnalyzer.getListOfUniqueCities(orders);

        assertEquals(2, cities.size(), "Should be exactly 2 unique cities");
        assertTrue(cities.contains("Pinsk"), "Pinsk should be in the List");
        assertTrue(cities.contains("Smolensk"), "Smolensk should be in the List");
    }

    @Test
    void getTotalIncomeListForAllCompletedOrdersTest(){
        Order order1 = Order.builder().customer(customer1).items(List.of(book)).status(OrderStatus.DELIVERED).build();
        Order order2 = Order.builder().customer(customer2).items(List.of(laptop)).status(OrderStatus.DELIVERED).build();

        List<Order> orders = List.of(order1, order2);

        double totalIncome = OrderAnalyzer.getTotalIncomeListForAllCompletedOrders(orders);

        // book: 150 * 10 = 1500, laptop: 10 * 1200 = 12000, total = 13500
        assertEquals(13500.0,totalIncome, "Total income should be 13500");
    }

    @Test
    void getMostPopularProductBySalesTest(){
        Order order1 = Order.builder().items(List.of(laptop)).build();
        Order order2 = Order.builder().items(List.of(book)).build();
        Order order3 = Order.builder().items(List.of(tShirt)).build();

        List<Order> orders = List.of(order1, order2, order3);
        List<String> popularProducts = OrderAnalyzer.getMostPopularProductsBySales(orders);

        assertEquals(2, popularProducts.size());
        assertTrue(popularProducts.contains("book"));
        assertTrue(popularProducts.contains("tShirt"));
        assertEquals("book", popularProducts.get(0), "Should be sorted alphabetically");
        assertEquals("tShirt", popularProducts.get(1));
    }

    @Test
    void getAverageCheckForDeliveredOrdersTest(){
        Order order1 = Order.builder().items(List.of(laptop)).status(OrderStatus.DELIVERED).build();
        Order order2 = Order.builder().items(List.of(book)).status(OrderStatus.DELIVERED).build();
        Order order3 = Order.builder().items(List.of(tShirt)).status(OrderStatus.CANCELLED).build();

        List<Order> orders = List.of(order1, order2, order3);
        double avgCheck = OrderAnalyzer.getAverageCheckForDeliveredOrders(orders);

        // (1200 * 10 + 10 * 250) / 2 = 6750
        assertEquals(6750.0, avgCheck);
    }

    @Test
    void getCustomersWhoHaveMoreThanFiveOrdersTest(){
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            orders.add(Order.builder()
                    .customer(customer1)
                    .items(List.of(book))
                    .status(OrderStatus.DELIVERED).build());
        }

        orders.add(Order.builder().customer(customer2).items(List.of(laptop)).build());

        List<Customer> customers = OrderAnalyzer.getCustomersWhoHaveMoreThenFiveOrders(orders);

        assertEquals(1, customers.size(), "Should have exactly 1 frequent customer");
        assertEquals(customer1, customers.get(0), "Should be customer 1");
    }

    @Test
    void testEmptyOrdersList(){
        List<Order> emptyOrders = List.of();

        assertTrue(OrderAnalyzer.getListOfUniqueCities(emptyOrders).isEmpty(), "Cities shpuld be empty");
        assertEquals(0.0, OrderAnalyzer.getAverageCheckForDeliveredOrders(emptyOrders), 0.01, "Average check should be 0");
        assertTrue(OrderAnalyzer.getMostPopularProductsBySales(emptyOrders).isEmpty(), "Popular products should be empty");
        assertEquals(0.0, OrderAnalyzer.getTotalIncomeListForAllCompletedOrders(emptyOrders), 0.01, "Income should be 0");
        assertTrue(OrderAnalyzer.getCustomersWhoHaveMoreThenFiveOrders(emptyOrders).isEmpty(), "Customers should be empty");
    }

    @Test
    void testOrdersWithNoItems(){
        Order order = Order.builder()
                .customer(customer2)
                .items(Collections.emptyList())
                .status(OrderStatus.DELIVERED)
                .build();

        List<Order> orders = List.of(order);

        assertEquals(0.0, OrderAnalyzer.getTotalIncomeListForAllCompletedOrders(orders),0.01, "Income should be 0 for empty items");
        assertTrue(OrderAnalyzer.getMostPopularProductsBySales(orders).isEmpty(), "No product should be found");
    }

    @Test
    void testOrderItemWithNullProductName(){
        OrderItem nullNameItem = OrderItem.builder()
                .productName(null)
                .quantity(5)
                .price(10.0)
                .build();

        Order order = Order.builder()
                .items(List.of(nullNameItem))
                .status(OrderStatus.DELIVERED)
                .customer(customer1)
                .build();

        List<Order> orders = List.of(order);

        List<String> popular = OrderAnalyzer.getMostPopularProductsBySales(orders);
        assertTrue(popular.isEmpty(), "Should ignore items with null product name");
    }

    @Test
    void testCustomerWithExactlyFiveOrders(){
        List<Order> orders = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            orders.add(Order.builder()
                    .customer(customer1)
                    .items(List.of(book))
                    .status(OrderStatus.DELIVERED)
                    .build());
        }

        List<Customer> result = OrderAnalyzer.getCustomersWhoHaveMoreThenFiveOrders(orders);
        assertTrue(result.isEmpty(), "Customer with exactly 5 orders should not be included needs more than 5");
    }

    @Test
    void testMultipleCustomerWithMoreThanFiveOrders(){
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            orders.add(Order.builder()
                    .customer(customer1)
                    .items(List.of(book))
                    .status(OrderStatus.DELIVERED)
                    .build());
        }

        for(int i = 0; i < 7; i++){
            orders.add(Order.builder()
                    .customer(customer2)
                    .items(List.of(laptop))
                    .status(OrderStatus.DELIVERED)
                    .build());
        }

        List<Customer> result = OrderAnalyzer.getCustomersWhoHaveMoreThenFiveOrders(orders);
        assertEquals(2, result.size(), "Should have 2 frequent customers");
        assertTrue(result.contains(customer1), "Shoul contain customer1");
        assertTrue(result.contains(customer2), "Should contain customer2");
    }

    @Test
    void testCustomerCityIsNull(){
        Customer customerWithNullCity = Customer.builder()
                .customerId("id")
                .city(null)
                .build();

        Order order = Order.builder()
                .customer(customerWithNullCity)
                .items(List.of(book))
                .build();

        List<Order> orders = List.of(order, Order.builder().customer(customer1).items(List.of(laptop)).build());

        List<String> cities = OrderAnalyzer.getListOfUniqueCities(orders);
        assertEquals(1, cities.size(), "Should ignore null city");
        assertEquals("Smolensk", cities.get(0), "Should contain only Smolensk");
    }

    @Test
    void testCancelledOrdersNotCountedInIncome(){
        Order completedOrder = Order.builder()
                .items(List.of(laptop)).status(OrderStatus.DELIVERED).build();

        Order cancelledOrder = Order.builder()
                .items(List.of(laptop)).status(OrderStatus.CANCELLED).build();

        List<Order> orders = List.of(cancelledOrder, completedOrder);

        double income = OrderAnalyzer.getTotalIncomeListForAllCompletedOrders(orders);

        assertEquals(2, orders.size(), "Orders should have 2 order");
        assertEquals(12000, income, 0.01, "Cancelled order should not contribute to income");

    }
}
