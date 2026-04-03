package log;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventLog {

    private final List<String> events = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LogObserver observer;

    private static EventLog instance = null;
    private EventLog() {}

    public static EventLog getInstance() {
        if (instance == null) {
            instance = new EventLog();
        }
        return instance;

    }

    public synchronized void addEvent(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = "[" + timestamp + "] :" + message;
        events.add(entry);
        System.out.println(entry); // print to console for debugging

        // Notify GUI log panel
        if (observer != null) {
            observer.onNewEvent(entry);
        }

    }

    public synchronized String getLatestEvent() {
        if (events.isEmpty()) return "";
        return events.getLast();
    }

    public synchronized List<String> getEvents() {
        return new ArrayList<>(events);
    }
    public synchronized void clear() {
        events.clear();
    }

    public synchronized void setObserver(LogObserver observer) {
        this.observer = observer;
    }

    public synchronized void saveToFile(String filePath) {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("   ============ Coffee Shop logger ========== \n");

            for (String event : events) {
                fw.write(event + "\n");
            }

            System.out.println("[EventLog] Log saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("[EventLog] Could not save log: " + e.getMessage());
        }
    }
}
