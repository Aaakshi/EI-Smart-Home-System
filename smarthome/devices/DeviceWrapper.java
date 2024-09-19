package smarthome.devices;

public class DeviceWrapper {
    private IDevice device;

    public DeviceWrapper(IDevice device) {
        this.device = device;
    }

    public IDevice getDevice() {
        return device;
    }
}
