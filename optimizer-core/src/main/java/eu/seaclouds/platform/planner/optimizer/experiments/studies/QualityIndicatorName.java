package eu.seaclouds.platform.planner.optimizer.experiments.studies;

public enum QualityIndicatorName {
HyperVolume, EPSILON, InvertedGenerationalDistance;

public String getIndicatorName() {
   switch (this) {
   case HyperVolume:
      return "HV";
   case EPSILON:
      return "EPSILON";
   case InvertedGenerationalDistance:
      return "IGD";
   default:// Epsilon
      return "EPSILON";
   }
}

}
