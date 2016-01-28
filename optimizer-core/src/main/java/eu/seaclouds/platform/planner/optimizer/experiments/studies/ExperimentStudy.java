package eu.seaclouds.platform.planner.optimizer.experiments.studies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import jmetal.util.JMException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.MOCellSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.NSGAIISearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.PAESSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.Random;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.SPEA2Search;
import eu.seaclouds.platform.planner.optimizer.operator.crossover.CrossoverOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.selection.SelectionOperatorName;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

public class ExperimentStudy {

   private Map<String, Object> appMap;

   private Map<String, Object> suitableCloudOffersMap;

   private Topology topology;

   private ArrayList<MetaHeuristic> engine = new ArrayList<MetaHeuristic>();

   private static Logger log = LoggerFactory.getLogger(ExperimentStudy.class);

   public ExperimentStudy(String appModel, String suitableCloudOffer, StudiesName name) throws JMException {

      initializeProblem(appModel, suitableCloudOffer);      
  
      switch (name) {
      case AllAlgorithms:
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology)
         .setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap,
               topology).setCrossoverProbability(0.5)
               .setMutationProbability(0.1).setPopulationSize(400)
               .setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap,
               topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap,
               topology).setMutationProbability(0.1).setArchiveSize(300).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(169).setMaxEvaluations(20000).build());
         break;
      case Random:
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         break;
      case NSGAII:
         //Best Conf: mutation 0.1, crossover 0.3 - 0.7 (best 0.5)
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.1).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.1).setMaxEvaluations(1000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMaxEvaluations(1000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setMaxEvaluations(1000).build());
//         
//         
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.8).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());

//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.4).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setPopulationSize(200).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.4).setMutationProbability(0.1).setPopulationSize(200).setMaxEvaluations(20000).build());
         
         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(16000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build()); //Best
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(300).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(300).setMaxEvaluations(20000).build());
         
         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_PERFORMANCE).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_PERFORMANCE_NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_AVAILABILITY).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());//3
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_AVAILABILITY_NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());//3
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_COST).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_COST_NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.RANDOMOFFER).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());//3
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.RANDOMOFFER_NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());//2
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.BEST_IN_ALL_PACandNumOfInstances).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationOperatorName(MutationOperatorName.CHOICE_BETWEEN_PAC_NUMofINSTANCES).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());//1
         
         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setSelectionOperatorName(SelectionOperatorName.BINARY_TOURNAMENT_2).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setSelectionOperatorName(SelectionOperatorName.BINARY_TOURNAMENT_2).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setSelectionOperatorName(SelectionOperatorName.BINARY_TOURNAMENT_2).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setSelectionOperatorName(SelectionOperatorName.BINARY_TOURNAMENT_2).setMutationProbability(0.1).setPopulationSize(400).setMaxEvaluations(20000).build());
         
         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).build());
         
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.08).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.1).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.12).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).build());
//         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).build());
         break;
      case MOCell:
         
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setCrossoverOperatoreName(CrossoverOperatorName.SinglePointCrossover).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.1).setMutationProbability(0.1).setArchiveSize(120).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.1).setArchiveSize(240).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(300).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.7).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.9).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
//         
//         
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.2).setMutationProbability(0.2).setArchiveSize(130).setPopulationSize(225).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.2).setArchiveSize(130).setPopulationSize(256).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.4).setMutationProbability(0.2).setArchiveSize(130).setPopulationSize(289).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.2).setArchiveSize(130).setPopulationSize(324).setMaxEvaluations(5000).build());
//         
//         
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.2).setMutationProbability(0.2).setArchiveSize(140).setPopulationSize(225).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.2).setArchiveSize(140).setPopulationSize(256).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.4).setMutationProbability(0.2).setArchiveSize(140).setPopulationSize(289).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.2).setArchiveSize(140).setPopulationSize(324).setMaxEvaluations(5000).build());
//         
//         
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.2).setMutationProbability(0.2).setArchiveSize(150).setPopulationSize(225).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.3).setMutationProbability(0.2).setArchiveSize(150).setPopulationSize(256).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.4).setMutationProbability(0.2).setArchiveSize(150).setPopulationSize(289).setMaxEvaluations(5000).build());
//         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.2).setArchiveSize(150).setPopulationSize(324).setMaxEvaluations(5000).build());
         
         break;
      case PAES:
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.01).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.05).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.2).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.3).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.4).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.5).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.6).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.7).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.8).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.9).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
//         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(1).setArchiveSize(150).setBiSections(5).setMaxEvaluations(100000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(50).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(100).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(150).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(200).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(300).setBiSections(5).setMaxEvaluations(20000).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).setMutationProbability(0.1).setArchiveSize(400).setBiSections(5).setMaxEvaluations(20000).build());
         
         break;
      case SPEA2:
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(100).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(121).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(144).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(169).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(196).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(225).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(100).setPopulationSize(256).setMaxEvaluations(20000).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).setCrossoverProbability(0.5).setMutationProbability(0.1).setArchiveSize(400).setPopulationSize(400).setMaxEvaluations(20000).build());
         break;
      default:
//         engine.add(new Random.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new NSGAIISearch.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new MOCellSearch.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new PAESSearch.Builder(appMap, suitableCloudOffersMap, topology).build());
         engine.add(new SPEA2Search.Builder(appMap, suitableCloudOffersMap, topology).build());
         // TODO:Complete with more methods
      }
      

   }

   public void startTest() throws JMException, IOException {

      new CustomStudy.Builder(engine).setIndependentRuns(100).build();

   }

   private void initializeProblem(String appModel, String suitableCloudOffer) {

      // Get app characteristics
      appMap = YAMLoptimizerParserNew.GetMAPofAPP(appModel);
      log.debug("appMap read");

      // Get cloud offers
      suitableCloudOffersMap = YAMLoptimizerParserNew
            .getMAPofCloudOffers(suitableCloudOffer);
      log.debug("suitableCloudOffersMap READ");

      topology = YAMLoptimizerParserNew.getApplicationTopology(appMap,
            suitableCloudOffersMap);
      log.debug("topology read");
   }

}
