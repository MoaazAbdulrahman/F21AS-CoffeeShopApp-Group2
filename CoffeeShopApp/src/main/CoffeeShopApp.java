package main;

import dataLoader.DataLoader;
import gui.OrderGUI;
import gui.SimulationController;
import log.EventLog;

import javax.swing.*;
import java.io.IOException;

public class CoffeeShopApp {

    private static final String MENU_PATH   = "data/menu.csv";
    private static final String ORDERS_PATH = "data/orders.csv";

    public static void main(String[] args) {

        EventLog log = EventLog.getInstance();
        log.addEvent("Application starting...");

        DataLoader loader = new DataLoader();

        // loading data
        try {
            loader.loadData(MENU_PATH, ORDERS_PATH);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading files:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        log.addEvent("Data loaded:  " + loader.getMenu().size() + " menu items, "  + loader.getOrderManager().getTotalOrderCount() + " orders.");

        // start both GUIs
        SwingUtilities.invokeLater(() -> {

            // CoffeeShop window
            OrderGUI orderGUI = new OrderGUI(loader.getMenu(), loader.getOrderManager());
            orderGUI.setLocation(30, 50);

            // simulator window
            SimulationController controller = new SimulationController(loader.getOrderManager());
            controller.start();


            int rightSide = orderGUI.getX() + orderGUI.getWidth() + 20;
            controller.getSimulatorGUI().setLocation(rightSide, 50);

            log.addEvent("GUI launched successfully.");
        });
    }
}