package eu.seaclouds.platform.planner.optimizer.metaheuristics;

public enum MetaHeuristicsName {
    RANDOM, NSGAII, MOCell, PAES, SPEA2, AsyncCellularGA, ElitistES, GenerationalGA, NonElitistES, SteadyStateGA, SyncCellularGA;

    public String getMetaHeuristicMethodName() {
        switch (this) {
        case RANDOM:
            return "RandomSearch";
        case NSGAII:
            return "NSGAII";
        case MOCell:
            return "MOCell";
        case PAES:
            return "PAES";
        case SPEA2:
            return "SPEA2";
        case AsyncCellularGA:
           return "AsyncCellularGA";
        case ElitistES:
           return "ElitistES";
        case GenerationalGA:
           return "GenerationalGA";
        case NonElitistES:
           return "NonElitistES";
        case SteadyStateGA:
           return "SteadyStateGA";
        case SyncCellularGA:
           return "SyncCellularGA";
        default:// Random
            return "RandomSearch";
        }
    }

}
