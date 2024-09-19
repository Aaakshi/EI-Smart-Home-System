package smarthome.scheduler;

import smarthome.devices.IDevice;

public class ScheduleTask {
    private IDevice device;
    private String time;
    private String command;

    // Constructor with time argument
    public ScheduleTask(IDevice device, String command, String time) {
        this.device = device;
        this.command = command;
        this.time = time;
    }

    // Getter methods
    public IDevice getDevice() {
        return device;
    }

    public String getTime() {
        return time;
    }

    public String getCommand() {
        return command;
    }
}
