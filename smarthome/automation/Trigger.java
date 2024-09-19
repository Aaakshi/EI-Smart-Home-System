package smarthome.automation;

public class Trigger {
    private String condition;
    private String comparison;
    private int value;
    private String action;

    public Trigger(String condition, String comparison, int value, String action) {
        this.condition = condition;
        this.comparison = comparison;
        this.value = value;
        this.action = action;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "{condition: \"" + condition + " " + comparison + " " + value +
                "\", action: \"" + action + "\"}";
    }
}
