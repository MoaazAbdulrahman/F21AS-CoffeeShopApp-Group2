package simulation;

import core.Order;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CustomerQueue {
    private final Queue<Order> queue = new LinkedList<>();
    private boolean simulationDone = false;

    public synchronized void addOrder(Order order) {
        queue.add(order);
        notify();
    }

    public synchronized Order removeOrder() throws InterruptedException {
        while (queue.isEmpty() && !simulationDone) {
            wait();
        }
        if (queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    public synchronized int getSize() {
        return queue.size();
    }
    public synchronized boolean isEmpty() {
        return simulationDone && queue.isEmpty();
    }

    public synchronized void stopSimulation() {
        simulationDone = true;
        notifyAll();
    }
    public synchronized List<Order> getCopy() {
        return new LinkedList<>(queue);
    }
}

