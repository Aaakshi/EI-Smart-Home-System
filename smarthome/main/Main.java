package smarthome.main;

import smarthome.devices.IDevice;
import smarthome.devices.Light;
import smarthome.devices.Thermostat;
import smarthome.devices.DoorLock;
import smarthome.factory.DeviceFactory;
import smarthome.observer.SmartHomeHub;
import smarthome.proxy.DeviceProxy;
import smarthome.automation.AutomationManager;
import smarthome.scheduler.Scheduler;
import smarthome.scheduler.ScheduleTask;
import smarthome.automation.Trigger;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize components
        SmartHomeHub hub = new SmartHomeHub();
        Scheduler scheduler = new Scheduler();
        AutomationManager automationManager = new AutomationManager(hub);

        // Create devices
        List<IDevice> devices = initializeDevices();
        Map<Integer, IDevice> deviceMap = new HashMap<>();
        for (IDevice device : devices) {
            deviceMap.put(device.getId(), new DeviceProxy(device));
        }

        // Add devices to hub
        for (IDevice device : deviceMap.values()) {
            hub.addDevice(device);
        }

        // Example commands
        automationManager.addSchedule(2, "06:00 Turn On");
        automationManager.addTrigger("temperature", ">", 75, "turnOff(1)");

        // Process commands
        processCommands(deviceMap, scheduler, automationManager);

        // Simulate actions
        scheduler.executeTasks();
        automationManager.checkTriggers();
        hub.updateDeviceStatus();

        // Print status report
        printStatusReport(hub, scheduler, automationManager);
    }

    private static List<IDevice> initializeDevices() {
        Scanner scanner = new Scanner(System.in);
        List<IDevice> devices = new ArrayList<>();

        System.out.println("Enter the number of devices:");
        int numberOfDevices = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numberOfDevices; i++) {
            System.out.println("Enter device details (id, type, status/temperature):");
            String input = scanner.nextLine();
            String[] inputParts = input.split(",\\s*");

            if (inputParts.length < 3) {
                System.out.println("Invalid input format.");
                continue;
            }

            try {
                int id = Integer.parseInt(inputParts[0]);
                String type = inputParts[1];
                String statusOrTemperature = inputParts[2];

                IDevice device = DeviceFactory.createDevice(type);
                if (device instanceof Light) {
                    if (statusOrTemperature.equals("on")) {
                        ((Light) device).turnOn();
                    } else if (statusOrTemperature.equals("off")) {
                        ((Light) device).turnOff();
                    }
                } else if (device instanceof Thermostat) {
                    ((Thermostat) device).setTemperature(Integer.parseInt(statusOrTemperature));
                } else if (device instanceof DoorLock) {
                    if (statusOrTemperature.equals("locked")) {
                        ((DoorLock) device).turnOn();
                    } else if (statusOrTemperature.equals("unlocked")) {
                        ((DoorLock) device).turnOff();
                    }
                } else {
                    System.out.println("Unknown device type.");
                    continue;
                }
                device.setId(id);
                devices.add(device);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in input.");
            } catch (Exception e) {
                System.out.println("Error processing device input: " + e.getMessage());
            }
        }
        return devices;
    }

    private static void processCommands(Map<Integer, IDevice> deviceMap, Scheduler scheduler,
            AutomationManager automationManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of commands:");
        int numberOfCommands = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numberOfCommands; i++) {
            System.out.println("Enter command:");
            String command = scanner.nextLine().trim();

            if (command.startsWith("turnOn")) {
                int id = Integer.parseInt(command.split("\\(")[1].split("\\)")[0]);
                IDevice device = deviceMap.get(id);
                if (device != null) {
                    device.turnOn();
                }
            } else if (command.startsWith("turnOff")) {
                int id = Integer.parseInt(command.split("\\(")[1].split("\\)")[0]);
                IDevice device = deviceMap.get(id);
                if (device != null) {
                    device.turnOff();
                }
            } else if (command.startsWith("setSchedule")) {
                String[] parts = command.split("\\(")[1].split("\\)")[0].split(",\\s*");
                int id = Integer.parseInt(parts[0]);
                String time = parts[1].replace("\"", "");
                String action = parts[2].replace("\"", "");
                IDevice device = deviceMap.get(id);
                if (device != null) {
                    scheduler.addTask(new ScheduleTask(device, action, time));
                }
            } else if (command.startsWith("addTrigger")) {
                String[] parts = command.split("\\(")[1].split("\\)")[0].split(",\\s*");
                String condition = parts[0].replace("\"", "");
                String comparison = parts[1].replace("\"", "");
                int value = Integer.parseInt(parts[2]);
                String action = parts[3].replace("\"", "");
                automationManager.addTrigger(condition, comparison, value, action);
            } else {
                System.out.println("Unknown command format: " + command);
            }
        }
    }

    private static void printStatusReport(SmartHomeHub hub, Scheduler scheduler,
            AutomationManager automationManager) {
        // Print status report
        System.out.println("Status Report:");
        StringBuilder statusReportBuilder = new StringBuilder();
        for (IDevice device : hub.getDevices()) {
            if (statusReportBuilder.length() > 0) {
                statusReportBuilder.append(". ");
            }
            statusReportBuilder.append(getDeviceStatus(device));
        }
        String statusReport = statusReportBuilder.toString();
        System.out.println(statusReport);

        // Print scheduled tasks
        System.out.println("Scheduled Tasks:");
        StringBuilder scheduledTasksBuilder = new StringBuilder("[");
        List<ScheduleTask> tasks = scheduler.getScheduledTasks();
        for (int i = 0; i < tasks.size(); i++) {
            ScheduleTask task = tasks.get(i);
            if (i > 0) {
                scheduledTasksBuilder.append(", ");
            }
            scheduledTasksBuilder.append("{device: ").append(task.getDevice().getId())
                    .append(", time: \"").append(task.getTime())
                    .append("\", command: \"").append(task.getCommand()).append("\"}");
        }
        scheduledTasksBuilder.append("]");
        System.out.println(scheduledTasksBuilder.toString());

        // Print automated triggers
        System.out.println("Automated Triggers:");
        StringBuilder automatedTriggersBuilder = new StringBuilder("[");
        List<Trigger> triggers = automationManager.getAutomatedTriggers();
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            if (i > 0) {
                automatedTriggersBuilder.append(", ");
            }
            automatedTriggersBuilder.append("{condition: \"").append(trigger.getCondition())
                    .append(" ").append(trigger.getComparison()).append(" ").append(trigger.getValue())
                    .append("\", action: \"").append(trigger.getAction()).append("\"}");
        }
        automatedTriggersBuilder.append("]");
        System.out.println(automatedTriggersBuilder.toString());
    }

    private static String getDeviceStatus(IDevice device) {
        String type = device.getClass().getSimpleName();
        int id = device.getId();
        String status = "";
        if (device instanceof Light) {
            status = ((Light) device).getStatus();
        } else if (device instanceof Thermostat) {
            status = "Set to " + ((Thermostat) device).getTemperature() + " degrees";
        } else if (device instanceof DoorLock) {
            status = ((DoorLock) device).getStatus();
        }
        return type.substring(0, 1).toUpperCase() + type.substring(1) + " " + id + " is " + status;
    }
}
