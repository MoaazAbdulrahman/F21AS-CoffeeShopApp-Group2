package tests;

import core.MenuItem;
import exceptions.InvalidMenuItemException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {

    @Test
    void testFoodItem() throws InvalidMenuItemException{
        MenuItem item = new MenuItem("FOOD-001", "cack", 3.5, "FOOD");
        assertEquals("FOOD-001", item.getId());
        assertEquals("FOOD", item.getCategory());
    }

    @Test
    void testInvalidId(){
        assertThrows(InvalidMenuItemException.class, () ->
        new MenuItem("BEV001", "Espresso", 2.50, "BEV")
        );
    }

    @Test
    void testEmptyId(){
        assertThrows(InvalidMenuItemException.class, () ->
                new MenuItem("", "Espresso", 2.50, "BEV")
        );
    }

    @Test
    void testNullId(){
        assertThrows(InvalidMenuItemException.class, () ->
                new MenuItem(null, "Espresso", 2.50, "BEV")
        );
    }

    @Test
    void testNegativeCost(){
        assertThrows(InvalidMenuItemException.class, () ->
                new MenuItem ("FOOD-001", "cack", -3.5, "FOOD"));
    }

    @Test
    void testEmptyDescription(){
        assertThrows(InvalidMenuItemException.class, () ->
                new MenuItem ("FOOD-001", "", -3.5, "FOOD"));
    }

    @Test
    void testNullDescription(){
        assertThrows(InvalidMenuItemException.class, () ->
                new MenuItem ("FOOD-001", null, -3.5, "FOOD"));
    }
}


