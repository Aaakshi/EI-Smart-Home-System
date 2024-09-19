package smarthome.devices;

public class Thermostat implements IDevice {
    private int temperature;
    private int id;

    // Default constructor
    public Thermostat() {
        this.temperature = 70; // default temperature
    }

    // Constructor with temperature parameter
    public Thermostat(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public void turnOn() {
        // Implementation for turning on the thermostat
    }

    @Override
    public void turnOff() {
        // Implementation for turning off the thermostat
    }

    @Override
    public String getStatus() {
        return "Set to " + temperature + " degrees";
    }

    @Override
    public String getType() {
        return "thermostat";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    // Method to set the temperature
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    // Method to get the current temperature
    public int getTemperature() {
        return temperature;
    }
}
