package smarthome.proxy;

import smarthome.devices.IDevice;

public class DeviceProxy implements IDevice {
    private IDevice realDevice;

    public DeviceProxy(IDevice realDevice) {
        this.realDevice = realDevice;
    }

    @Override
    public void turnOn() {
        realDevice.turnOn();
    }

    @Override
    public void turnOff() {
        realDevice.turnOff();
    }

    @Override
    public String getStatus() {
        return realDevice.getStatus();
    }

    @Override
    public String getType() {
        return realDevice.getType();
    }

    @Override
    public int getId() {
        return realDevice.getId();
    }

    @Override
    public void setId(int id) {
        realDevice.setId(id);
    }
}
