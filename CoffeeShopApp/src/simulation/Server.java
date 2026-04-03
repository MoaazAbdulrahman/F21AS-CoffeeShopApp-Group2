package simulation;

import core.MenuItem;
import core.Order;
import core.OrderCalculator;
import log.EventLog;
import java.util.concurrent.ThreadLocalRandom;

import java.util.List;
import java.util.Random;

public class Server implements Runnable {

    public enum State { IDLE, PROCESSING, WAITING }

    private final int serverId;
    private final CustomerQueue queue;
    private final SpeedController speedController;
    private final OrderCalculator calculator = new OrderCalculator();
    private final Random random = new Random();
    private final EventLog log = EventLog.getInstance();

    // Current state
    private volatile State currentState = State.IDLE;
    private volatile Order currentOrder = null;
    private volatile String statusMessage = "Waiting for new orders...";

    // Observer to get notified on state changes
    private ServerObserver observer;

    public Server(int serverId, CustomerQueue queue, SpeedController speedController) {
        this.serverId = serverId;
        this.queue = queue;
        this.speedController = speedController;
    }

    public void setObserver(ServerObserver observer) {
        this.observer = observer;
    }

    @Override
    public void run() {
        log.addEvent("Server " + serverId + " started.");

        while (true) {
            try {
                // change to IDLE while waiting for next order
                setState(State.IDLE, "Waiting for orders ... I am waiting ", null);

                // Wait here until an order is available - blocking
                Order order = queue.removeOrder();

                // if null simulation is done and queue is empty
                if (order == null) {
                    setState(State.IDLE, "Simulation ended.", null);
                    log.addEvent("Server " + serverId + " Thank you, no more work to do, I am going home. Goodbye.");
                    break;
                }

                // Start processing
                setState(State.PROCESSING, "Processing " + order.getCustomerId(), order);
                log.addEvent("Server " + serverId + " started processing: " + order.getCustomerId() + order.getItemCount() + " items");

                // Simulate processing time
                int processingTime = calculateProcessingTime(order);
                Thread.sleep((long) (processingTime * speedController.getSpeedMultiplier()));

                // Done processing
                double total = calculator.calculateTotalPrice(order);
                double discount = calculator.calculateDiscount(order);

                String doneMsg = String.format("Completed %s - Total: £%.2f%s", order.getCustomerId(), total,
                        discount > 0 ? String.format(" (saved £%.2f)", discount) : "");

                setState(State.WAITING, doneMsg, null);
                log.addEvent("Server " + serverId + " " + doneMsg);

                // Take some time before accepting the next order - for simulation
                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.addEvent("Server " + serverId + " interrupted.");
                break;
            }
        }
    }

    private int calculateProcessingTime(Order order) {
        int totalTime = 0;

        for (MenuItem item : order.getItems()) {
            totalTime += switch (item.getCategory().toUpperCase()) {
                case "BEV" -> ThreadLocalRandom.current().nextInt(4000, 5000);
                case "FOOD" -> ThreadLocalRandom.current().nextInt(7000, 9000);
                case "OTHER" -> ThreadLocalRandom.current().nextInt(2000, 3000);
                default -> 0;
            };
        }
        return totalTime;
    }


    private void setState(State state, String message, Order order) {
        this.currentState = state;
        this.statusMessage = message;
        this.currentOrder = order;

        if (observer != null) {
            observer.onServerStateChanged(this);
        }
    }

    // GUI Getters
    public int getServerId() {return serverId;}
    public Order getCurrentOrder() {return currentOrder; }
    public State getCurrentState() {return currentState;}
    public String getStatusMessage() { return statusMessage;}
}