package core;

public class MenuItem {
    private final String id;
    private final String description;
    private final double cost;
    private final String category;

    public MenuItem(String id, String description, double cost, String category){
        this.id = id;
        this.description = description;
        this.cost = cost;
        this.category = category;
    }

    public String getId(){return id;}
    public String getDescription(){return description;}
    public double getCost() {return cost;}
    public String getCategory() {return category;}
}
