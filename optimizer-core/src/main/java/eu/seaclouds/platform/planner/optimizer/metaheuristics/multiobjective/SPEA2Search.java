package eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.EqualSolutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic.Builder;
import eu.seaclouds.platform.planner.optimizer.operator.crossover.CrossoverOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.selection.SelectionOperatorName;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblem;

public class SPEA2Search extends MetaHeuristic {

   static Logger log = LoggerFactory.getLogger(SPEA2Search.class);

   public static class Builder extends MetaHeuristic.Builder<Builder> {

      private int populationSize = 100;
      private int maxEvaluations = 20000;
      private int archiveSize = 100;

      private CrossoverOperatorName crossoverOperatorName = CrossoverOperatorName.MultiPointCrossover; // DEFAULT
      private double crossoverProbability = 0.9;
      private MutationOperatorName mutationOperatorName = MutationOperatorName.ALL_KIND; // DEFAULT
      private double mutationProbability = 0;
      private SelectionOperatorName selectionOperatorName = SelectionOperatorName.BINARY_TOURNAMENT; // DEFAULT

      public Builder(Map<String, Object> appMap,
            Map<String, Object> suitableCloudOffersMap, Topology topology) {
         super(appMap, suitableCloudOffersMap, topology);
      }

      public Builder setPopulationSize(int populationSize) {
         if (populationSize != 0)
            this.populationSize = populationSize;
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

      public Builder setCrossoverOperatoreName(
            CrossoverOperatorName crossoverOperatorName) {
         this.crossoverOperatorName = crossoverOperatorName;
         return this;
      }

      public Builder setCrossoverProbability(double crossoverProbability) {
         if (crossoverProbability != 0)
            this.crossoverProbability = crossoverProbability;
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

      public Builder setSelectionOperatorName(
            SelectionOperatorName selectionOperatorName) {
         this.selectionOperatorName = selectionOperatorName;
         return this;
      }

      public SPEA2Search build() throws JMException {
         return new SPEA2Search(this);
      }
   }

   public SPEA2Search(Builder builder) throws JMException {
      super(builder);

      this.populationSize = builder.populationSize;
      this.maxEvaluations = builder.maxEvaluations;
      this.archiveSize = builder.archiveSize;
      this.crossoverOperatorName = builder.crossoverOperatorName;
      this.crossoverProbability = builder.crossoverProbability;
      this.mutationOperatorName = builder.mutationOperatorName;
      if (builder.mutationProbability != 0)
         this.mutationProbability = builder.mutationProbability;
      this.selectionOperatorName = builder.selectionOperatorName;

      this.problem = new SeaCloudsProblem(this.appMap,
            this.suitableCloudOfferMap, this.topology);

      this.algorithm = new SPEA2(this.problem);
      this.metaHeuristicName = this.getAlgorithm().getClass().getSimpleName();

      // Algorithm parameters
      this.algorithm.setInputParameter("populationSize", this.populationSize);
      this.algorithm.setInputParameter("maxEvaluations", this.maxEvaluations);
      this.algorithm.setInputParameter("archiveSize", this.archiveSize);

      // Crossover Operator
      this.parameters = new HashMap();
      this.parameters.put("probability", this.crossoverProbability);
      // this.crossover = CrossoverFactory.getCrossoverOperator(
      // "MultiPointCrossover", this.parameters);
      this.crossover = CrossoverFactory.getCrossoverOperator(
            this.crossoverOperatorName.getCrossoverOperatorValue(), this.parameters);

      this.parameters.clear();

      // Mutation Operator
      this.parameters.put("probability", this.mutationProbability);
      // this.mutation = MutationFactory.getMutationOperator(
      // "PACandNumOfInstancesMutation", this.parameters);
      this.mutation = MutationFactory.getMutationOperator(
            this.mutationOperatorName.getMutationOperatorValue(), this.parameters);

      this.parameters.clear();

      // Selection Operator
      // this.selection =
      // SelectionFactory.getSelectionOperator("BinaryTournament",
      // this.parameters);
      if((builder.selectionOperatorName.getSelectionOperatorValue().equals(SelectionOperatorName.BEST_SOLUTION.getSelectionOperatorValue())) || (builder.selectionOperatorName.getSelectionOperatorValue().equals(SelectionOperatorName.WORST_SOLUTION.getSelectionOperatorValue())) ){
//       Comparator comparator = new DominanceComparator();
       Comparator comparator = new EqualSolutions(); 
       this.parameters.put("comparator", comparator);
    }
      this.selection = SelectionFactory.getSelectionOperator(
            this.selectionOperatorName.getSelectionOperatorValue(), this.parameters);

      // Add the operators to the algorithm
      this.algorithm.addOperator("crossover", this.crossover);
      this.algorithm.addOperator("mutation", this.mutation);
      this.algorithm.addOperator("selection", this.selection);

   }

}
