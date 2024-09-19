package smarthome.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Scheduler {
    private List<ScheduleTask> tasks = new ArrayList<>();

    public void addTask(ScheduleTask task) {
        tasks.add(task);
    }

    public void executeTasks() {
        // Implement task execution logic here
    }

    public List<ScheduleTask> getScheduledTasks() {
        return tasks;
    }
}
