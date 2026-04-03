package simulation;

public interface QueueObserver {
    void onQueueChanged(CustomerQueue queue);

    void onSimulationFinished();
}