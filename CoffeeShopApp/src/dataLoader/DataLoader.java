package dataLoader;

import core.MenuItem;
import core.Order;
import core.OrderManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataLoader {

    private Map<String, MenuItem> menu;
    private OrderManager orderManager; // To be used later by GUI

    public void loadData(String menuFilePath, String ordersFilePath) throws IOException {

        // load menu file data
        MenuLoader menuLoader = new MenuLoader();
        menu = menuLoader.readMenu(menuFilePath);

        // load order data
        OrderLoader orderLoader = new OrderLoader();
        List<Order> orders = orderLoader.readOrders(ordersFilePath, menu);

        // init orderManager and add orders to it
        orderManager = new OrderManager();
        for (Order order : orders){
            orderManager.addOrder(order);
        }
        System.out.println("==== All orders are loaded Successfully ===== ");
    }

    public Map<String, MenuItem> getMenu() {
        return menu;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }
}
