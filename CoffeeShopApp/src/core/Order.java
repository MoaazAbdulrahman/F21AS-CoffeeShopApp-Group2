package core;

import java.util.ArrayList;
import java.util.List;
import exceptions.InvalidOrderException;

public class Order {
    private final String customerId;
    private final String timestamp;
    private final List<MenuItem> items;
    private static final String CUSTOMER_ID_PATTERN = "^CUST-\\d{3}$"; // CUST-XXX

    public Order(String customerId, String timestamp) throws InvalidOrderException{
        // validate inputs
        validateId(customerId);
        validateTimestamp(timestamp);

        // init attributes
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item){
        items.add(item);

    }

    public int getItemCount() {
        return items.size();
    }

    public int countCategoryItems(String category){
        int count = 0;
        for (MenuItem item: items ){
            if (item.getCategory().equalsIgnoreCase(category)){
                count++;

            }
        }
        return count;
    }

    public double getTotalCost(){
        double totalCost = 0;
        for(MenuItem item : items){
            totalCost += item.getCost();
        }
        return totalCost;
    }

    public String getCustomerId() { return customerId; }

    public String getTimestamp() { return timestamp; }

    public List<MenuItem> getItems() { return new ArrayList<>(items); }


    private void validateId(String customerId) throws InvalidOrderException {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidOrderException("Customer ID cannot be null or empty.");
        }

        if (!customerId.matches(CUSTOMER_ID_PATTERN)) {
            throw new InvalidOrderException("Invalid customer ID: format must be CUST-XXX ");
        }
    }

    private void validateTimestamp(String timestamp) throws InvalidOrderException {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            throw new InvalidOrderException("Timestamp cannot be null or empty.");
        }
    }
}
