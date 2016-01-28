package eu.seaclouds.platform.planner.optimizer.operator.selection;

public enum SelectionOperatorName {
    BINARY_TOURNAMENT, BINARY_TOURNAMENT_2, BEST_SOLUTION, WORST_SOLUTION, RANDOM;

    public String getSelectionOperatorValue() {
        switch (this) {
        case BINARY_TOURNAMENT:
            return "BinaryTournament";
        case BINARY_TOURNAMENT_2:
            return "BinaryTournament2";
        case BEST_SOLUTION:
           return "BestSolutionSelection";
        case WORST_SOLUTION:
           return "WorstSolutionSelection";
        default:// BINARY_TOURNAMENT
            return "BinaryTournament";
        }
    }
}
