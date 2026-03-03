
package gui;

import core.MenuItem;
import core.Order;
import core.OrderCalculator;
import core.OrderManager;
import exceptions.InvalidOrderException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class OrderGUI extends JFrame {

    private final Map<String, MenuItem> menu;
    private final OrderManager orderManager;
    private final OrderCalculator calculator = new OrderCalculator();

    private final List<MenuItem> selectedItems = new ArrayList<>();

    private JTextArea billArea;
//    Private JLabel statusLabel;
    private JLabel itemCountLabel;

    private int customerCounter = 1;

    public OrderGUI(Map<String, MenuItem> menu, OrderManager manager){
        this.menu = menu;
        this.orderManager = manager;

        setSize(800, 600);
        setTitle("Coffee Shop Order System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e){
//                handleExit();
            }
        });

        buildGUI();
        setVisible(true);

    }
    private void buildGUI(){
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Coffee Shop — Order System");
        add(title, BorderLayout.NORTH);

        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildMenuPanel(), buildBillPanel());
        splitter.setDividerLocation(400);
        add(splitter, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane buildMenuPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (MenuItem item : menu.values()){
            JCheckBox cbox = new JCheckBox(item.getDescription() + "- $ " + item.getCost());
            cbox.addActionListener(e -> {
                if (cbox.isSelected()) selectedItems.add(item);
                else selectedItems.remove(item);
                itemCountLabel.setText("Items selected: " + selectedItems.size());
                updateBill();
            });
            panel.add(cbox);
        }
        return new JScrollPane(panel);
    }

    private JScrollPane buildBillPanel() {
        billArea = new JTextArea();

        billArea.setText("No items selected.");

        return new JScrollPane(billArea);
    }

    private JPanel buildBottomPanel(){
        JPanel panel = new JPanel(new BorderLayout());

        JPanel buttonsRow = new JPanel();
        JButton completeBtn = new JButton("Complete Order");
        JButton reportBtn = new JButton("Report");
        JButton clearBtn = new JButton("Clear");

        buttonsRow.add(completeBtn);
        buttonsRow.add(reportBtn);
        buttonsRow.add(clearBtn);

//        completeBtn.addActionListener(e -> completeOrder());
//        reportBtn.addActionListener(e -> showReport());
//        clearBtn.addActionListener(e -> clearSelection());
        itemCountLabel = new JLabel("Items selected: 0");

        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.add(itemCountLabel, BorderLayout.EAST);
        panel.add(buttonsRow, BorderLayout.CENTER);
        panel.add(infoRow,   BorderLayout.SOUTH);

        return panel;
    }

    private void updateBill(){
        if (selectedItems.isEmpty()){
            billArea.setText("No Items Selected. Please add items ...");
            return;
        }
        try{
            Order tempOrder = new Order("CUST-000", "Temp");

            for (MenuItem item : selectedItems) tempOrder.addItem(item);

            double subtotal = tempOrder.getTotalCost();
            double discount = calculator.calculateDiscount(tempOrder);
            double total = calculator.calculateTotalPrice(tempOrder);
            String rule = calculator.getAppliedRule(tempOrder);

            StringBuilder output = new StringBuilder();

            for(MenuItem item : selectedItems){
                output.append(item.getDescription()+ " - $ "+item.getCost() + "\n");

            }
            output.append("\n Subtotal: $" + subtotal);
            output.append("\n Discount: $" + discount + "(" + rule + ")");
            output.append("\n Total: $" + total);

            billArea.setText(output.toString());

        } catch (InvalidOrderException ex) {
            billArea.setText("Error calculating order.");
        }
    }


}