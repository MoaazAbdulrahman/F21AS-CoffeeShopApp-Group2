package core;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String customerId;
    private final String timestamp;
    private final List<MenuItem> items;


    public Order(String customerId, String timestamp){
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
}
