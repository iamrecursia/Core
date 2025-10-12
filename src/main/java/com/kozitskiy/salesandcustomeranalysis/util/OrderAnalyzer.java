package com.kozitskiy.salesandcustomeranalysis.util;

import com.kozitskiy.salesandcustomeranalysis.entity.Customer;
import com.kozitskiy.salesandcustomeranalysis.entity.Order;
import com.kozitskiy.salesandcustomeranalysis.entity.OrderItem;
import com.kozitskiy.salesandcustomeranalysis.entity.OrderStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class OrderAnalyzer {

    private OrderAnalyzer(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static double getTotalIncomeListForAllCompletedOrders(List<Order> orders){
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum();
    }

    public static List<String> getListOfUniqueCities(List<Order> orders){
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<String> getMostPopularProductsBySales(List<Order> orders){
        if (orders == null || orders.isEmpty()){
            return List.of();
        }

        // Grouping by name and counting quantity for each
        Map<String, Integer> productToTotalQuantity = orders.stream()
                .flatMap(order -> order.getItems().stream()
                        .filter(item -> item.getProductName()!=null))
                        .collect(Collectors.groupingBy(
                                OrderItem::getProductName,
                                Collectors.summingInt(OrderItem::getQuantity)
                        ));
        if (productToTotalQuantity.isEmpty()){
            return List.of();
        }

        int maxQuantity = Collections.max(productToTotalQuantity.values());

        return productToTotalQuantity.entrySet().stream()
                .filter(entry -> entry.getValue() == maxQuantity)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    public static double getAverageCheckForDeliveredOrders(List<Order> orders){
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getQuantity() * item.getPrice())
                        .sum()
                )
                .average()
                .orElse(0.0);
    }

    public static List<Customer> getCustomersWhoHaveMoreThenFiveOrders(List<Order> orders){
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
