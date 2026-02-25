package dataLoader;
import core.MenuItem;
import core.Order;
import exceptions.InvalidMenuItemException;
import exceptions.InvalidOrderException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderLoader {
    public List<Order> readOrders(String filePath, Map<String, MenuItem> menu) throws IOException {
        Map<String, Order> orderMap = new java.util.LinkedHashMap<>(); // use linked hashmap to sort the orders

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // skip header row
                if (line.trim().isEmpty()) continue; //skip empty row
                try {
                    processOrderLine(line, lineNumber, menu, orderMap);
                } catch (InvalidOrderException e) {
                    System.out.println("Invalid order item : " + e.getMessage());
                }
            }
        }
        List<Order> orders = new ArrayList<>(orderMap.values());
        System.out.println("============" + " order Loaded = " + orders.size() + "============");
        return orders;
    }

    private void processOrderLine(String line, int lineNumber, Map<String, MenuItem> menu,
                                  Map<String, Order> orderMap) throws InvalidOrderException {
        String[] elements = line.split(",", 3);
        if (elements.length < 3) {
            throw new InvalidOrderException("Line " + lineNumber + "has missing values : " + line);
        }
        String timestamp = elements[0].trim();
        String customerId = elements[1].trim();
        String itemId = elements[2].trim();

        // validate customer id
        if (customerId.isEmpty()) {
            throw new InvalidOrderException("Line " + lineNumber + ": customerId is missing.");
        }

        // Get existing order or create a new one
        if (!orderMap.containsKey(customerId)) {
            Order newOrder = new Order(customerId, timestamp);
            orderMap.put(customerId, newOrder);
        }

        // Add the item to the order
        MenuItem item = menu.get(itemId);
        orderMap.get(customerId).addItem(item);
    }
}