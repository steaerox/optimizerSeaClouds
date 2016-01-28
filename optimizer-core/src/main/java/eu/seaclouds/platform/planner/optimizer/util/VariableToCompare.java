package eu.seaclouds.platform.planner.optimizer.util;

public enum VariableToCompare {
    PERFORMANCE, AVAILABILITY, COST;

    public int getIntegerValue() {
        switch (this) {
        case PERFORMANCE:
            return 0;
        case AVAILABILITY:
            return 1;
        case COST:
            return 2;
        default:// PERFORMANCE
            return 0;
        }
    }

    public String getStringValue() {
        switch (this) {
        case PERFORMANCE:
            return "Performance";
        case AVAILABILITY:
            return "Availability";
        case COST:
            return "Cost";
        default:// PERFORMANCE
            return "Performance";
        }
    }
}
