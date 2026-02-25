package tests;

import core.MenuItem;
import core.Order;

import exceptions.InvalidMenuItemException;
import exceptions.InvalidOrderException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class OrderTest {
    private MenuItem espresso;
    private MenuItem cack;
    private MenuItem cookies;

    @BeforeEach
    void init() throws InvalidMenuItemException {
        espresso = new MenuItem("BEV-001", "Espresso", 2.50, "BEV");
        cack = new MenuItem("FOOD-001", "cack", 2.80, "FOOD");
        cookies = new MenuItem("FOOD-002", "cookies", 2.50, "FOOD");
    }

    @Test
    void testOrderCreation() throws InvalidOrderException {
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        assertEquals("CUST-001", order.getCustomerId());
        assertEquals("2025-03-01 08:05:12", order.getTimestamp());
        assertEquals(0, order.getItemCount());
    }

    @Test
    void testNullCustomerId(){
        assertThrows(InvalidOrderException.class, () -> new Order(null, "2025-03-01 08:05:12"));
    }

    @Test
    void testEmptyCustomerId(){
        assertThrows(InvalidOrderException.class, () -> new Order("", "2025-03-01 08:05:12"));
    }

    @Test
    void testInvalidCustomerId(){
        assertThrows(InvalidOrderException.class, () -> new Order("CUST001", "2025-03-01 08:05:12"));
    }

    @Test
    void testAddItem() throws InvalidOrderException{
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        order.addItem(cack);
    }

    @Test
    void testAddMultipleItems() throws InvalidOrderException{
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        order.addItem(cack);
        order.addItem(cookies);
        order.addItem(espresso);

    }

    @Test
    void testCountItems() throws InvalidOrderException {
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        order.addItem(cack);
        order.addItem(cookies);
        order.addItem(espresso);

        assertEquals(1, order.countCategoryItems("BEV"));
        assertEquals(2, order.countCategoryItems("FOOD"));
        assertEquals(0, order.countCategoryItems("OTHER"));
    }

    @Test
    void testCountEmptyOrder() throws InvalidOrderException {
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        assertEquals(0, order.countCategoryItems("BEV"));
    }

    @Test
    void testTotalCost() throws InvalidOrderException {
        Order order = new Order("CUST-001", "2025-03-01 08:05:12");
        order.addItem(espresso);    // 2.50
        order.addItem(cookies);  //2.50
        assertEquals(5.0, order.getTotalCost(), 0.001);
    }

}
