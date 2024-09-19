package smarthome.observer;

import smarthome.devices.IDevice;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmartHomeHub {
    private List<IDevice> devices = new ArrayList<>();

    public void addDevice(IDevice device) {
        devices.add(device);
    }

    public void updateDeviceStatus() {
        for (IDevice device : devices) {
            System.out.println(device.getType() + " status: " + device.getStatus());
        }
    }

    // Get the list of all devices
    public List<IDevice> getDevices() {
        return new ArrayList<>(devices);
    }

    // Find a device by ID
    public Optional<IDevice> getDeviceById(int id) {
        return devices.stream().filter(device -> device.getId() == id).findFirst();
    }

    // Generate a status report
    public String getStatusReport() {
        StringBuilder report = new StringBuilder();
        for (IDevice device : devices) {
            report.append(device.getType())
                    .append(" ")
                    .append(device.getId())
                    .append(" is ")
                    .append(device.getStatus())
                    .append(". ");
        }
        return report.toString().trim();
    }
}
