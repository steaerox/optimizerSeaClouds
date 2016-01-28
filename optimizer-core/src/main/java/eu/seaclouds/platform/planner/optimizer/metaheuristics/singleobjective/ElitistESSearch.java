package eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective;

import java.util.HashMap;
import java.util.Map;

import jmetal.metaheuristics.paes.PAES;
import jmetal.metaheuristics.singleObjective.evolutionStrategy.ElitistES;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic.Builder;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblem;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblemSingleObj;

public class ElitistESSearch extends MetaHeuristic {

   public static class Builder extends MetaHeuristic.Builder<Builder> {

      // Requirement: lambda must be divisible by mu
      private int mu = 6;
      private int lambda = 36;

      private int maxEvaluations = 18000;
      private MutationOperatorName mutationOperatorName = MutationOperatorName.ALL_KIND; // DEFAULT
      private double mutationProbability = 0;

      public Builder(Map<String, Object> appMap,
            Map<String, Object> suitableCloudOffersMap, Topology topology) {
         super(appMap, suitableCloudOffersMap, topology);
      }
      
      public Builder setMu(int mu) {
         if (mu <= 0)
          //TODO: Error
            return this;
         this.mu = mu;
         return this;
      }
      
      public Builder setLambda(int lambda) {
         if (lambda <= 0)
            //TODO: Error // Requirement: lambda must be divisible by mu
            return this;
         this.lambda = lambda;
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

      public ElitistESSearch build() throws JMException {
         return new ElitistESSearch(this);
      }
   }

   public ElitistESSearch(Builder builder) throws JMException {
      super(builder);

      this.maxEvaluations = builder.maxEvaluations;
      this.mu = builder.mu;
      this.lambda = builder.lambda;
      this.mutationOperatorName = builder.mutationOperatorName;
      if (builder.mutationProbability != 0)
         this.mutationProbability = builder.mutationProbability;

      this.problem = new SeaCloudsProblemSingleObj(this.appMap,
            this.suitableCloudOfferMap, this.topology);

      this.algorithm = new ElitistES(problem, mu, lambda);
      this.metaHeuristicName = this.getAlgorithm().getClass().getSimpleName();

      // Algorithm parameters
      this.algorithm.setInputParameter("maxEvaluations", this.maxEvaluations);

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
