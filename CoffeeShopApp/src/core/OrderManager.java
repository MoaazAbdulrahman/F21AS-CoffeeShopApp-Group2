package core;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;



public class OrderManager {
    private final List<Order> ordersList;
    private final Map<String, Order> ordersByCustomer;
    private final Map<String, Integer> itemOrderCount;

    private final OrderCalculator orderCalculator;

    public OrderManager(){
        ordersList = new ArrayList<>();
        ordersByCustomer = new HashMap<>();
        itemOrderCount = new HashMap<>();
        orderCalculator = new OrderCalculator();
    }

    public void addOrder(Order order){

        ordersList.add(order);
        ordersByCustomer.put(order.getCustomerId(), order);

        for (MenuItem item : order.getItems()){
            String itemId = item.getId();
            itemOrderCount.put(itemId, itemOrderCount.getOrDefault(itemId, 0) + 1);
        }
    }
    public Order getOrderByCustomer(String customerId){
        return ordersByCustomer.get(customerId);
    }
    public int getItemOrderCount(String itemId) {
        return itemOrderCount.getOrDefault(itemId, 0);
    }
    public int getTotalOrderCount() {
        return ordersList.size();
    }

    public String generateReport(Map<String, MenuItem> menu) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Coffee Shop Report ===\n\n");
        sb.append("Total Orders: ").append(ordersList.size()).append("\n\n");

        sb.append("Menu Items:\n");

        for (String itemId : menu.keySet()) {
            MenuItem item = menu.get(itemId);
            int count = itemOrderCount.getOrDefault(itemId, 0);

            sb.append(itemId)
                    .append(" - ")
                    .append(item.getDescription())
                    .append(" | ordered: ")
                    .append(count)
                    .append(" | price: £")
                    .append(String.format("%.2f", item.getCost()))
                    .append("\n");
        }

        sb.append("\nOrders:\n");

        double grandTotal = 0;

        for (Order order : ordersList) {
            double discount = orderCalculator.calculateDiscount(order);
            double total = orderCalculator.calculateTotalPrice(order);
            grandTotal += total;

            sb.append(order.getCustomerId())
                    .append(" | items: ").append(order.getItemCount())
                    .append(" | total: £").append(String.format("%.2f", total))
                    .append("\n");
        }

        sb.append("\nGrand Total: £")
                .append(String.format("%.2f", grandTotal))
                .append("\n");

        return sb.toString();
    }
}
