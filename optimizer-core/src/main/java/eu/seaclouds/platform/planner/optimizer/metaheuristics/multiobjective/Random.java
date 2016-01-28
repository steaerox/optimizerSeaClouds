package eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective;

import java.util.Map;

import jmetal.metaheuristics.randomSearch.RandomSearch;
import jmetal.util.JMException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic.Builder;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblem;

public class Random extends MetaHeuristic {

   static Logger log = LoggerFactory.getLogger(Random.class);

   public static class Builder extends MetaHeuristic.Builder<Builder> {

      private int maxEvaluations = 20000;

      public Builder(Map<String, Object> appMap,
            Map<String, Object> suitableCloudOffersMap, Topology topology) {
         super(appMap, suitableCloudOffersMap, topology);
      }

      public Builder setMaxEvaluations(int maxEvaluations) {
         if (maxEvaluations != 0)
            this.maxEvaluations = maxEvaluations;
         return this;
      }

      public Random build() throws JMException {
         return new Random(this);
      }
   }

   public Random(Builder builder) {
      super(builder);
      this.maxEvaluations = builder.maxEvaluations;
      
      this.problem = new SeaCloudsProblem(this.appMap,
            this.suitableCloudOfferMap, this.topology);

      this.algorithm = new RandomSearch(this.problem);
      this.metaHeuristicName = this.getAlgorithm().getClass().getSimpleName();

      // Algorithm parameters
      this.algorithm.setInputParameter("maxEvaluations", this.maxEvaluations);
   }
}
