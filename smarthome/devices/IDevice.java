package smarthome.devices;

public interface IDevice {
    void turnOn();

    void turnOff();

    String getStatus();

    void setId(int id);

    int getId();

    String getType();
}
