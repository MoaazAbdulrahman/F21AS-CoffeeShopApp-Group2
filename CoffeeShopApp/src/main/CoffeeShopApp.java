package main;

import dataLoader.DataLoader;
import gui.OrderGUI;

import javax.swing.*;
import java.io.IOException;

public class CoffeeShopApp {

    private static final String MENU_PATH = "data/menu.csv";
    private static final String ORDERS_PATH = "data/orders.csv";

    public static void main(String[] args) {

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

        // start GUI
        SwingUtilities.invokeLater(() -> {
            new OrderGUI(loader.getMenu(), loader.getOrderManager());
        });
    }
}