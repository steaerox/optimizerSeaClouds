package eu.seaclouds.platform.planner.optimizer.operator.mutation;

public enum MutationOperatorName {
   ALL_KIND, CHOICE_BETWEEN_PAC_NUMofINSTANCES, BEST_IN_ALL_PACandNumOfInstances, BEST_AVAILABILITY_NUMofINSTANCES, BEST_AVAILABILITY, BEST_COST_NUMofINSTANCES, BEST_COST, BEST_PERFORMANCE_NUMofINSTANCES, BEST_PERFORMANCE, NUMofINSTANCES, RANDOMOFFER_NUMofINSTANCES, RANDOMOFFER;

   public String getMutationOperatorValue() {
      switch (this) {
      case ALL_KIND:
         return "AllKindOfMutation";
      case CHOICE_BETWEEN_PAC_NUMofINSTANCES:
         return "ChoiceBetweenBestPACandNumOfInstancesMutation";
      case BEST_IN_ALL_PACandNumOfInstances:
         return "BestInAllPACandNumOfInstancesMutation";
      case BEST_AVAILABILITY_NUMofINSTANCES:
         return "BestAvailabilityAndNumOfInstancesMutation";
      case BEST_AVAILABILITY:
         return "BestAvailabilityMutation";
      case BEST_COST_NUMofINSTANCES:
         return "BestCostAndNumOfInstancesMutation";
      case BEST_COST:
         return "BestCostMutation";
      case BEST_PERFORMANCE_NUMofINSTANCES:
         return "BestPerformanceAndNumOfInstancesMutation";
      case BEST_PERFORMANCE:
         return "BestPerformanceMutation";
      case NUMofINSTANCES:
         return "NumOfInstancesMutation";
      case RANDOMOFFER_NUMofINSTANCES:
         return "RandomOfferAndInstancesMutation";
      case RANDOMOFFER:
         return "RandomOfferMutation";
      default:// BestInAllPACandNumOfInstancesMutation
         return "BestInAllPACandNumOfInstancesMutation";
      }
   }
}
