package smarthome.factory;

import smarthome.devices.IDevice;
import smarthome.devices.Light;
import smarthome.devices.Thermostat;
import smarthome.devices.DoorLock;

public class DeviceFactory {
    public static IDevice createDevice(String type) {
        switch (type.toLowerCase()) { // Convert type to lower case for consistency
            case "light":
                return new Light();
            case "thermostat":
                return new Thermostat(); // Use default constructor
            case "door":
                return new DoorLock(); // Use default constructor
            default:
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}
