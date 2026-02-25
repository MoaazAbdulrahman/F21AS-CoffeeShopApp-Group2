package core;
import exceptions.InvalidMenuItemException;

public class MenuItem {
    private final String id;
    private final String description;
    private final double cost;
    private final String category;

    private static final String ID_PATTERN = "^[A-Z]+-\\d{3}$"; //BEV-001

    public MenuItem (String id, String description, double cost, String category) throws InvalidMenuItemException {
        //validate inputs
        validateId(id);
        validateCost(cost);
        validateDescription(description);

        // init attributes
        this.id = id;
        this.description = description;
        this.cost = cost;
        this.category = category;
    }

    public String getId(){return id;}
    public String getDescription(){return description;}
    public double getCost() {return cost;}
    public String getCategory() {return category;}


    private void validateId(String id) throws InvalidMenuItemException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidMenuItemException("itemId cannot be empty or null.");
        }
        if (!id.matches(ID_PATTERN)) {
            throw new InvalidMenuItemException("Invalid ID format: Expected format: (e.g. BEV-001) ... Your id = " + id);
        }
    }

    private void validateCost(double cost) throws InvalidMenuItemException {
        if (cost < 0) {
            throw new InvalidMenuItemException("Cost cannot be negative ... your cost" + cost);
        }
    }

    private void validateDescription(String description) throws InvalidMenuItemException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidMenuItemException("Description cannot be null or empty.");
        }
    }
}
