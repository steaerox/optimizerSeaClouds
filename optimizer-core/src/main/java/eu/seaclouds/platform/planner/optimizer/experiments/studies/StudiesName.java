package eu.seaclouds.platform.planner.optimizer.experiments.studies;

public enum StudiesName {
   AllAlgorithms, Random, NSGAII, MOCell, PAES, SPEA2;

   public String getStudyName() {
      switch (this) {
      case AllAlgorithms:
         return "AllAlgorithms";
      case Random:
         return "Random";
      case NSGAII:
         return "NSGAII";
      case MOCell:
         return "MOCell";
      case PAES:
         return "PAES";
      case SPEA2:
         return "SPEA2";
      default:// Random
         return "Random";
      }
   }

}
