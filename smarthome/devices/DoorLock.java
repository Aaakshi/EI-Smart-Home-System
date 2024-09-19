package smarthome.devices;

public class DoorLock implements IDevice {
    private int id;
    private boolean isLocked;

    @Override
    public void turnOn() {
        isLocked = true; // Assuming turning on locks the door
    }

    @Override
    public void turnOff() {
        isLocked = false; // Assuming turning off unlocks the door
    }

    @Override
    public String getStatus() {
        return isLocked ? "Locked" : "Unlocked";
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getType() {
        return "DoorLock";
    }
}
