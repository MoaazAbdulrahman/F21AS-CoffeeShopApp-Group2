package gui;

import core.Order;
import core.OrderCalculator;
import log.EventLog;
import log.LogObserver;
import simulation.CustomerQueue;
import simulation.Server;
import simulation.ServerObserver;
import simulation.QueueObserver;

import simulation.SpeedController;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimulationGUI extends JFrame implements QueueObserver, ServerObserver, LogObserver {

    private final CustomerQueue queue;
    private final SpeedController speedController;
    private final EventLog log = EventLog.getInstance();

    private JTextArea queueArea;
    private JLabel queueCountLabel;
    private final Map<Integer, JLabel> serverStatus  = new HashMap<>();
    private final Map<Integer, JLabel> serverOrder   = new HashMap<>();
    private final Map<Integer, JLabel> serverDetails = new HashMap<>();
    private JTextArea logArea;
    private JLabel speedLabel;


    public SimulationGUI(CustomerQueue queue, SpeedController speedController, List<Server> servers) {

        this.queue = queue;
        this.speedController = speedController;

        setTitle("Coffee Shop -  Simulator");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        handleStop();
                    }
                });

        log.setObserver(this);
        buildGUI(servers);
        setVisible(true);

        for (String event : log.getEvents()) {
            appendToLog(event);
        }
    }

    private void buildGUI(List<Server> servers) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Coffee Shop - Simulator");
        add(title, BorderLayout.NORTH);

        JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildQueuePanel(), buildServersPanel(servers));
        topSplit.setDividerLocation(280);

        JPanel centre = new JPanel(new BorderLayout());
        centre.add(topSplit, BorderLayout.CENTER);
        centre.add(buildLogPanel(), BorderLayout.SOUTH);

        add(centre, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }


    private JScrollPane buildQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Queue"));

        queueCountLabel = new JLabel("0 customers waiting");
        panel.add(queueCountLabel, BorderLayout.NORTH);

        queueArea = new JTextArea();
        queueArea.setEditable(false);
        queueArea.setText("Waiting for new customers...");
        panel.add(new JScrollPane(queueArea), BorderLayout.CENTER);

        return new JScrollPane(panel);
    }

    private JPanel buildServersPanel(List<Server> servers) {
        JPanel panel = new JPanel(new GridLayout(1, servers.size(), 10, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Servers"));

        for (Server server : servers) {
            panel.add(buildServerCard(server));
        }
        return panel;
    }

    private JPanel buildServerCard(Server server) {
        int id = server.getServerId();

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createTitledBorder("Server " + id));

        JLabel statusLbl  = new JLabel("IDLE");
        JLabel orderLbl   = new JLabel("Waiting for orders...");
        JLabel detailsLbl = new JLabel(" ");

        card.add(statusLbl);
        card.add(orderLbl);
        card.add(detailsLbl);

        serverStatus.put(id, statusLbl);
        serverOrder.put(id, orderLbl);
        serverDetails.put(id, detailsLbl);

        return card;
    }

    private JPanel buildLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Event Log"));
        panel.setPreferredSize(new Dimension(0, 180));

        logArea = new JTextArea();
        logArea.setEditable(false);
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel speedPanel = new JPanel();
        JButton minusBtn = new JButton("-");
        JButton plusBtn = new JButton("+");
        speedLabel = new JLabel(speedController.getSpeedLabel());

        minusBtn.addActionListener(e -> {
            speedController.decreaseSpeed();
            speedLabel.setText(speedController.getSpeedLabel());
            log.addEvent("Speed changed to: " + speedController.getSpeedLabel());
        });

        plusBtn.addActionListener(e -> {
            speedController.increaseSpeed();
            speedLabel.setText(speedController.getSpeedLabel());
            log.addEvent("Speed changed to: " + speedController.getSpeedLabel());
        });

        speedPanel.add(new JLabel("Speed:"));
        speedPanel.add(minusBtn);
        speedPanel.add(speedLabel);
        speedPanel.add(plusBtn);

        JButton stopBtn = new JButton("Stop Simulation");
        stopBtn.setForeground(Color.RED);
        stopBtn.addActionListener(e -> handleStop());
        panel.add(speedPanel, BorderLayout.WEST);
        panel.add(stopBtn, BorderLayout.EAST);

        return panel;
    }

    // Observers

    @Override
    public void onQueueChanged(CustomerQueue queue) {
        SwingUtilities.invokeLater(this::refreshQueuePanel);
    }

    @Override
    public void onSimulationFinished() {
        SwingUtilities.invokeLater(() ->
                queueCountLabel.setText("All orders processed. ..."));
    }

    @Override
    public void onServerStateChanged(Server server) {
        SwingUtilities.invokeLater(() -> refreshServerPanel(server));
    }

    @Override
    public void onNewEvent(String event) {
        SwingUtilities.invokeLater(() -> appendToLog(event));
    }


     // refresh
    private void refreshQueuePanel() {
        List<Order> q_copy = queue.getCopy();
        int size = q_copy.size();

        queueCountLabel.setText(size == 0 ? "Queue is empty" : size + " customers are waiting");
        if (q_copy.isEmpty()) {
            queueArea.setText("Queue is empty.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Order order : q_copy) {
            sb.append(String.format("%-12s  %d item(s)%n", order.getCustomerId(), order.getItemCount()));
        }
        queueArea.setText(sb.toString());
        queueArea.setCaretPosition(0);
    }

    private void refreshServerPanel(Server server) {
        int id = server.getServerId();

        JLabel statusLbl = serverStatus.get(id);
        JLabel orderLbl = serverOrder.get(id);
        JLabel detailsLbl = serverDetails.get(id);

        if (statusLbl == null) return;

        switch (server.getCurrentState()) {
            case IDLE -> {
                statusLbl.setText("IDLE");
                orderLbl.setText("Waiting for orders...");
                detailsLbl.setText(" ");
            }
            case PROCESSING -> {
                Order order = server.getCurrentOrder();
                statusLbl.setText("Processing");
                orderLbl.setText(order != null ? order.getCustomerId() : "");
                detailsLbl.setText(order != null ? order.getItemCount() + " items" + String.format("%.2f", order.getTotalCost()) : " ");
            }
            case WAITING -> {
                statusLbl.setText("DONE");
                orderLbl.setText(server.getStatusMessage());
                detailsLbl.setText(" ");
            }
        }
    }

    private void appendToLog(String event) {
        logArea.append(event + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // Stop
    private void handleStop() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Stop simulator and save log file?",
                "Stop simulator", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            queue.stopSimulation();
            log.saveToFile("simulation_log.txt");
            dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            queue.stopSimulation();
            dispose();
        }
    }
}
