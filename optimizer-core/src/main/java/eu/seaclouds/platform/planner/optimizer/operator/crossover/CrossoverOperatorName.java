package eu.seaclouds.platform.planner.optimizer.operator.crossover;

public enum CrossoverOperatorName {
    MultiPointCrossover, SinglePointCrossover;

    public String getCrossoverOperatorValue() {
        switch (this) {
        case MultiPointCrossover:
            return "MultiPointCrossover";
        case SinglePointCrossover:
            return "SinglePointCrossover";
        default:// MultiPointCrossover
            return "MultiPointCrossover";
        }
    }
}
