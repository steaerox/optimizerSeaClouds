package eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective;

import java.util.HashMap;
import java.util.Map;

import jmetal.metaheuristics.paes.PAES;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic.Builder;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblem;

public class PAESSearch extends MetaHeuristic {

   public static class Builder extends MetaHeuristic.Builder<Builder> {

      private int maxEvaluations = 20000;
      private int archiveSize = 100;
      private int biSections = 5;
      private MutationOperatorName mutationOperatorName = MutationOperatorName.ALL_KIND; // DEFAULT
      private double mutationProbability = 0;

      public Builder(Map<String, Object> appMap,
            Map<String, Object> suitableCloudOffersMap, Topology topology) {
         super(appMap, suitableCloudOffersMap, topology);
      }

      public Builder setBiSections(int biSections) {
         if (biSections != 0)
            this.biSections = biSections;
         return this;
      }

      public Builder setArchiveSize(int archiveSize) {
         if (archiveSize != 0)
            this.archiveSize = archiveSize;
         return this;
      }

      public Builder setMaxEvaluations(int maxEvaluations) {
         if (maxEvaluations != 0)
            this.maxEvaluations = maxEvaluations;
         return this;
      }

      public Builder setMutationOperatorName(
            MutationOperatorName mutationOperatorName) {
         this.mutationOperatorName = mutationOperatorName;
         return this;
      }

      public Builder setMutationProbability(double mutationProbability) {
         this.mutationProbability = mutationProbability;
         return this;
      }

      public PAESSearch build() throws JMException {
         return new PAESSearch(this);
      }
   }

   public PAESSearch(Builder builder) throws JMException {
      super(builder);

      this.maxEvaluations = builder.maxEvaluations;
      this.archiveSize = builder.archiveSize;
      this.biSections = builder.biSections;
      this.mutationOperatorName = builder.mutationOperatorName;
      if (builder.mutationProbability != 0)
         this.mutationProbability = builder.mutationProbability;

      this.problem = new SeaCloudsProblem(this.appMap,
            this.suitableCloudOfferMap, this.topology);

      this.algorithm = new PAES(this.problem);
      this.metaHeuristicName = this.getAlgorithm().getClass().getSimpleName();

      // Algorithm parameters
      this.algorithm.setInputParameter("maxEvaluations", this.maxEvaluations);
      this.algorithm.setInputParameter("archiveSize", this.archiveSize);
      this.algorithm.setInputParameter("biSections", this.biSections);

      // Mutation Operator
      this.parameters = new HashMap();
      this.parameters.put("probability", this.mutationProbability);
      // this.mutation = MutationFactory.getMutationOperator(
      // "PACandNumOfInstancesMutation", this.parameters);
      this.mutation = MutationFactory.getMutationOperator(
            this.mutationOperatorName.getMutationOperatorValue(), this.parameters);

      this.parameters.clear();

      // Add the operators to the algorithm
      this.algorithm.addOperator("mutation", this.mutation);

   }

}
