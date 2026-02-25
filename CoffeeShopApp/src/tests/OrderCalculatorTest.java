package tests;
import core.MenuItem;
import core.Order;
import core.OrderCalculator;
import exceptions.InvalidMenuItemException;
import exceptions.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class OrderCalculatorTest {

    private OrderCalculator calculator;

    // create multiple items for testing
    MenuItem bev1;
    MenuItem bev2;
    MenuItem bev3;
    MenuItem food1;
    MenuItem food2;
    MenuItem other1;

    @BeforeEach
    void init() throws InvalidMenuItemException{
        calculator = new OrderCalculator();
        bev1 = new MenuItem("BEV-001", "Espresso",2.50, "BEV");
        bev2 = new MenuItem("BEV-002", "Cappuccino",3.00, "BEV");
        bev3 = new MenuItem("BEV-003", "Latte",4.50, "BEV");
        food1 = new MenuItem("FOOD-001", "Cack",5.0, "FOOD");
        food2 = new MenuItem("FOOD-002", "Cookies",2.50, "FOOD");
        other1 = new MenuItem("OTHER-001", "Still Water",1.50, "OTHER");

    }

    @Test
    void testComboDeal() throws InvalidOrderException {
        Order order = new Order("CUST-002", "2025-03-01 08:00:00");
        // Comobo deal = 1 bev + 2 food ... get 20% discount
        order.addItem(bev1); // 2.50
        order.addItem(food1); // 5.0
        order.addItem(food2); // 2.50
        assertEquals(2.0, calculator.calculateDiscount(order), 0.001);
        assertEquals(8.0, calculator.calculateTotalPrice(order), 0.001);
    }

    @Test
    void testComboNoDeal() throws InvalidOrderException{
        Order order = new Order("CUST-002", "2025-03-01 08:00:00");
        // Comobo deal = 1 bev + 2 food ... get 20% discount
        order.addItem(bev1); // 2.50
        order.addItem(food1); // 5.0
        order.addItem(other1); // 1.50
        assertEquals(0.0, calculator.calculateDiscount(order), 0.001);
        assertEquals(9.0, calculator.calculateTotalPrice(order), 0.001);
    }

    @Test
    void testBulkDeal() throws InvalidOrderException{
        Order order = new Order("CUST-002", "2025-03-01 08:00:00");
        // 3+ bevs get 10% discount
        order.addItem(bev1);
        order.addItem(bev2);
        order.addItem(bev3);

        assertEquals(1.0, calculator.calculateDiscount(order), 0.001);
        assertEquals(9.0, calculator.calculateTotalPrice(order), 0.001);

    }

    @Test
    void testBulkNoDeal() throws InvalidOrderException{
        Order order = new Order("CUST-002", "2025-03-01 08:00:00");
        // 3+ bevs get 10% discount
        order.addItem(bev1);
        order.addItem(bev2);

        assertEquals(0.0, calculator.calculateDiscount(order), 0.001);
        assertEquals(5.50, calculator.calculateTotalPrice(order), 0.001);

    }

}
