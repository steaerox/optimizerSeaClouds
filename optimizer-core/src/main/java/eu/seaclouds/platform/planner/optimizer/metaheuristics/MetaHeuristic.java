package eu.seaclouds.platform.planner.optimizer.metaheuristics;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.ExtractParetoFront;
import jmetal.util.JMException;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.ObjectiveComparator;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.NSGAIISearch.Builder;
import eu.seaclouds.platform.planner.optimizer.operator.crossover.CrossoverOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.selection.SelectionOperatorName;
import eu.seaclouds.platform.planner.optimizer.util.VariableToCompare;

public class MetaHeuristic {

   protected static final double MAX_MILLIS_EXECUTING = 20000;

   static Logger log = LoggerFactory.getLogger(MetaHeuristic.class);

   protected String metaHeuristicName;

   protected Problem problem = null; // The problem to solve
   protected Algorithm algorithm = null; // The algorithm to use
   protected Operator crossover = null; // Crossover operator
   protected Operator mutation = null; // Mutation operator
   protected Operator selection = null; // Selection operator

   protected HashMap parameters = null; // Operator parameters

   protected QualityIndicator indicators = null; // Object to get quality
                                                 // indicators

   // Single-objective parameters
   protected int mu = 6;
   protected int lambda = 36;
   protected boolean needSpecialInitialization;

   // Default parameters
   protected int populationSize;
   protected int maxEvaluations;
   protected int archiveSize;
   protected int biSections;
   protected int feedBack;

   protected CrossoverOperatorName crossoverOperatorName;
   protected double crossoverProbability;
   protected MutationOperatorName mutationOperatorName;
   protected double mutationProbability = 0;
   protected SelectionOperatorName selectionOperatorName;

   protected Map<String, Object> appMap;
   protected Map<String, Object> suitableCloudOfferMap;
   protected Topology topology;

   public String getMetaHeuristicName() {
      return metaHeuristicName;
   }

   public void setMetaHeuristicName(String metaHeuristicName) {
      this.metaHeuristicName = metaHeuristicName;
   }

   public Problem getProblem() {
      return problem;
   }

   public Algorithm getAlgorithm() {
      return algorithm;
   }

   public Operator getCrossover() {
      return crossover;
   }

   public Operator getMutation() {
      return mutation;
   }

   public Operator getSelection() {
      return selection;
   }

   public HashMap getParameters() {
      return parameters;
   }

   public QualityIndicator getIndicators() {
      return indicators;
   }

   public int getPopulationSize() {
      return populationSize;
   }

   public int getMaxEvaluations() {
      return maxEvaluations;
   }

   public int getArchiveSize() {
      return archiveSize;
   }

   public int getBiSections() {
      return biSections;
   }

   public int getFeedBack() {
      return feedBack;
   }

   public CrossoverOperatorName getCrossoverOperatorName() {
      return crossoverOperatorName;
   }

   public double getCrossoverProbability() {
      return crossoverProbability;
   }

   public MutationOperatorName getMutationOperatorName() {
      return mutationOperatorName;
   }

   public double getMutationProbability() {
      return mutationProbability;
   }

   public SelectionOperatorName getSelectionOperatorName() {
      return selectionOperatorName;
   }

   public Map<String, Object> getAppMap() {
      return appMap;
   }

   public Map<String, Object> getSuitableCloudOfferMap() {
      return suitableCloudOfferMap;
   }

   public Topology getTopology() {
      return topology;
   }

   public SolutionSet computeOptimizationProblem() throws JMException,
         ClassNotFoundException {

      SolutionSet population = new SolutionSet();

      // Execute the Algorithm
      long initTime = System.currentTimeMillis();
      population = algorithm.execute();
      long endTime = System.currentTimeMillis();
      long estimatedTime = endTime - initTime;

      // Result messages
      log.info("\n\nOptimizer execution time= " + (((estimatedTime)) / 1000.0)
            + " seconds\n");
      if (estimatedTime > MAX_MILLIS_EXECUTING)
         log.warn("\nOtimizer does not have good Performance. More than "
               + (MAX_MILLIS_EXECUTING) / 1000.0 + " seconds\n");
      return population;
   }

   public static class Builder<T extends Builder> {

      private Map<String, Object> appMap;
      private Map<String, Object> suitableCloudOffersMap;
      private Topology topology;

      public Builder(Map<String, Object> appMap,
            Map<String, Object> suitableCloudOffersMap, Topology topology) {
         this.appMap = appMap;
         this.suitableCloudOffersMap = suitableCloudOffersMap;
         this.topology = topology;
      }

      public MetaHeuristic build() throws JMException {
         return new MetaHeuristic(this);
      }
   }

   public MetaHeuristic(Builder builder) {
      
      this.appMap = builder.appMap;
      this.suitableCloudOfferMap = builder.suitableCloudOffersMap;
      this.topology = builder.topology;

   }

}
