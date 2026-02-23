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
}
