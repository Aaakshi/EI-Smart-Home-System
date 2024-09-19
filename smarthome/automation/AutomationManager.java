package smarthome.automation;

import smarthome.observer.SmartHomeHub;
import smarthome.devices.IDevice;
import smarthome.devices.Thermostat;

import java.util.*;

public class AutomationManager {
    private SmartHomeHub hub;
    private Map<String, Runnable> triggers = new HashMap<>();
    private Map<Integer, String> schedules = new HashMap<>();
    private List<Trigger> triggerList = new ArrayList<>(); // Store triggers as Trigger objects

    public AutomationManager(SmartHomeHub hub) {
        this.hub = hub;
    }

    /**
     * Adds a trigger that maps a condition to an action.
     *
     * @param condition  The condition (e.g., temperature).
     * @param comparison The comparison operator (e.g., >, <).
     * @param value      The threshold value for the condition.
     * @param action     The action to perform if the condition is met.
     */
    public void addTrigger(String condition, String comparison, int value, String action) {
        String triggerKey = condition + " " + comparison + " " + value;
        Runnable actionRunnable = () -> executeAction(action);
        triggers.put(triggerKey, actionRunnable);
        Trigger trigger = new Trigger(condition, comparison, value, action);
        triggerList.add(trigger); // Store trigger as a Trigger object
    }

    /**
     * Executes an action, typically controlling a device.
     *
     * @param action The action in the form of a command string (e.g.,
     *               "turnOff(1)").
     */
    private void executeAction(String action) {
        try {
            int id = Integer.parseInt(action.replaceAll("[^0-9]", ""));
            IDevice device = hub.getDeviceById(id).orElse(null);
            if (device != null) {
                device.turnOff(); // Assuming the action is to turn off the device.
                System.out.println("Executed action: Turned off device " + id);
            } else {
                System.out.println("Device not found for action: " + action);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid action format: " + action);
        } catch (Exception e) {
            System.out.println("Error executing action: " + action + ". " + e.getMessage());
        }
    }

    /**
     * Checks all triggers and executes the associated actions if the condition is
     * met.
     */
    public void checkTriggers() {
        for (Map.Entry<String, Runnable> entry : triggers.entrySet()) {
            String trigger = entry.getKey();
            Runnable action = entry.getValue();

            // Split trigger into condition, comparison, and value
            String[] parts = trigger.split(" ");
            if (parts.length == 3) {
                String condition = parts[0];
                String comparison = parts[1];
                int value = Integer.parseInt(parts[2]);

                // Get the current temperature (or other values) from the hub or a specific
                // device
                int currentTemperature = getCurrentTemperature();

                // Check if the condition is met
                if (condition.equals("temperature") && comparison.equals(">")) {
                    if (currentTemperature > value) {
                        System.out.println("Trigger met: " + trigger);
                        action.run(); // Execute the associated action
                    }
                }
            }
        }
    }

    /**
     * Retrieves the current temperature from the thermostat in the hub.
     *
     * @return The current temperature or 0 if no thermostat is found.
     */
    private int getCurrentTemperature() {
        Optional<IDevice> thermostat = hub.getDevices().stream()
                .filter(device -> device instanceof Thermostat)
                .findFirst();

        // Return the temperature or 0 if no thermostat is found
        return thermostat.map(device -> ((Thermostat) device).getTemperature()).orElse(0);
    }

    /**
     * Adds a schedule for a device.
     *
     * @param deviceId The ID of the device.
     * @param schedule The schedule in string format.
     */
    public void addSchedule(int deviceId, String schedule) {
        schedules.put(deviceId, schedule);
    }

    /**
     * Retrieves scheduled tasks in a readable format.
     *
     * @return A formatted string containing the scheduled tasks.
     */
    public String getScheduledTasks() {
        StringBuilder scheduleReport = new StringBuilder();
        schedules.forEach((id, schedule) -> scheduleReport.append("[{device: ").append(id)
                .append(", schedule: \"").append(schedule).append("\"}] "));
        return scheduleReport.toString().trim();
    }

    /**
     * Retrieves automated triggers in a readable format.
     *
     * @return A list of Trigger objects.
     */
    public List<Trigger> getAutomatedTriggers() {
        return triggerList; // Return the list of Trigger objects
    }
}
