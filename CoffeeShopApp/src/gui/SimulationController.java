package gui;

import core.Order;
import core.OrderManager;
import log.EventLog;
import simulation.CustomerQueue;
import simulation.Producer;
import simulation.Server;
import simulation.SpeedController;

import java.util.ArrayList;
import java.util.List;

public class SimulationController {

    private static final int num_servers = 2;

    private final CustomerQueue queue;
    private final SpeedController speedController;
    private final EventLog log = EventLog.getInstance();

    private final List<Server> servers = new ArrayList<>();
    private Producer producer;
    private SimulationGUI  simulatorGUI;

    public SimulationController(OrderManager orderManager) {

        this.queue = new CustomerQueue();
        this.speedController = new SpeedController();

        // Build server threads
        for (int i = 1; i <= num_servers; i++) {
            servers.add(new Server(i, queue, speedController));
        }

        // Build producer thread with all orders loaded
        this.producer = new Producer(queue, orderManager.getOrdersList());
    }

    public void start() {
        // Create GUI 
        simulatorGUI = new SimulationGUI(queue, speedController, servers);

        producer.setObserver(simulatorGUI);

        for (Server server : servers) {
            server.setObserver(simulatorGUI);
        }

        // Start server threads
        for (Server server : servers) {
            Thread serverThread = new Thread(server, "Server :" + server.getServerId());
            serverThread.setDaemon(true);
            serverThread.start();
            log.addEvent("Server " + server.getServerId() + " thread started");
        }

        // Start producer thread
        Thread producerThread = new Thread(producer, "producer");
        producerThread.setDaemon(true);
        producerThread.start();
        log.addEvent("producer thread started, simulation is running ...");
    }

    // getters
    public CustomerQueue getQueue(){
        return queue;
    }

    public SpeedController getSpeedController() {
        return speedController;
    }

    public List<Server> getServersList(){
        return servers;
    }

    public SimulationGUI getSimulatorGUI(){
        return simulatorGUI;
    }

}