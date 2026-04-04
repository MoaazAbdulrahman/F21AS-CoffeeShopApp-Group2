package simulation;

public class SpeedController {
    public enum Speed { SLOW, NORMAL, FAST }
    private Speed currentSpeed = Speed.NORMAL;

    public synchronized void increaseSpeed() {
        switch (currentSpeed) {
            case SLOW -> currentSpeed = Speed.NORMAL;
            case NORMAL -> currentSpeed = Speed.FAST;
            case FAST -> {}
        }
    }
    
    public synchronized void decreaseSpeed() {
        switch (currentSpeed) {
            case FAST -> currentSpeed = Speed.NORMAL;
            case NORMAL -> currentSpeed = Speed.SLOW;
            case SLOW -> {} // already at min
        }
    }
    
    public synchronized double getSpeedMultiplier() {
        return switch (currentSpeed) {
            case SLOW -> 2.0;
            case NORMAL -> 1.0;
            case FAST -> 0.4;
        };
    }


    public synchronized String getSpeedLabel() {
        return switch (currentSpeed) {
            case SLOW -> "Slow";
            case NORMAL -> "Normal";
            case FAST -> "Fast";
        };
    }

    public synchronized Speed getCurrentSpeed() {
        return currentSpeed;
    }
}