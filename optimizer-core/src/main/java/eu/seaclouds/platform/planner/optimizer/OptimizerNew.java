/**
 * Copyright 2014 SeaClouds
 * Contact: SeaClouds
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.seaclouds.platform.planner.optimizer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.comparators.EpsilonDominanceComparator;
import jmetal.util.comparators.EpsilonObjectiveComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristicsName;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.MOCellSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.NSGAIISearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.PAESSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.Random;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.SPEA2Search;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.AsynchronousCellularGASearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.ElitistESSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.GenerationalGASearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.NonElitistESSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.SteadyStateGASearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.SynchronousCellularGASearch;
import eu.seaclouds.platform.planner.optimizer.operator.crossover.CrossoverOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.selection.SelectionOperatorName;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

public class OptimizerNew {

   private final static double EPSILON = 0.001;

   private Map<String, Object> appMap;

   private Map<String, Object> suitableCloudOffersMap;

   private Topology topology;

   private static Logger log = LoggerFactory.getLogger(OptimizerNew.class);

   private MetaHeuristic engine;

   public OptimizerNew(String appModel, String suitableCloudOffer,
         MetaHeuristicsName name) throws JMException {

      initializeProblem(appModel, suitableCloudOffer);

      switch (name) {
      case RANDOM:
         engine = new Random.Builder(appMap, suitableCloudOffersMap, topology)
               .setMaxEvaluations(200000).build();
         break;
      case NSGAII:
         // engine = new NSGAIISearch.Builder(appMap, suitableCloudOffersMap,
         // topology).setPopulationSize(30).setMaxEvaluations(1000).build();
         engine = new NSGAIISearch.Builder(appMap, suitableCloudOffersMap,
               topology).setCrossoverProbability(0.5)
               .setMutationProbability(0.1).setPopulationSize(400)
               .setMaxEvaluations(20000).build();
         break;
      case MOCell:
         // engine = new MOCellSearch.Builder(appMap, suitableCloudOffersMap,
         // topology).setCrossoverProbability(0.2).setMutationProbability(0.2).setArchiveSize(120).setPopulationSize(256).setMaxEvaluations(100000).build();
         engine = new MOCellSearch.Builder(appMap, suitableCloudOffersMap,
               topology).setCrossoverProbability(0.5)
               .setMutationProbability(0.1).setArchiveSize(400)
               .setPopulationSize(400).setMaxEvaluations(20000).build();
         break;
      case PAES:
         engine = new PAESSearch.Builder(appMap, suitableCloudOffersMap,
               topology).setMutationProbability(0.1).setArchiveSize(300)
               .setBiSections(5).setMaxEvaluations(20000).build();
         break;
      case SPEA2:
         engine = new SPEA2Search.Builder(appMap, suitableCloudOffersMap,
               topology).setCrossoverProbability(0.5)
               .setMutationProbability(0.1).setArchiveSize(100)
               .setPopulationSize(169).setMaxEvaluations(20000).build();
         break;
      case AsyncCellularGA:
         engine = new AsynchronousCellularGASearch.Builder(appMap,
               suitableCloudOffersMap, topology).setMutationProbability(0.1)
               .setCrossoverProbability(0.5).setPopulationSize(400)
               .setMaxEvaluations(20000).build();
         break;
      case ElitistES:
         engine = new ElitistESSearch.Builder(appMap, suitableCloudOffersMap,
               topology).setMutationProbability(0.1).setMu(6).setLambda(36)
               .setMaxEvaluations(20000).build();
         break;
      case GenerationalGA:
         engine = new GenerationalGASearch.Builder(appMap,
               suitableCloudOffersMap, topology).setMutationProbability(0.1)
               .setCrossoverProbability(0.5).setMaxEvaluations(20000).build();
         break;
      case NonElitistES:
         engine = new NonElitistESSearch.Builder(appMap,
               suitableCloudOffersMap, topology).setMutationProbability(0.1)
               .setMu(6).setLambda(36).setMaxEvaluations(20000).build();
         break;
      case SteadyStateGA:
         engine = new SteadyStateGASearch.Builder(appMap,
               suitableCloudOffersMap, topology).setMutationProbability(0.1)
               .setCrossoverProbability(0.5).setMaxEvaluations(20000).build();
         break;
      case SyncCellularGA:
         // populationSize as a multiple of NxN: 3x3, 4x4, 5x5, etc.
         engine = new SynchronousCellularGASearch.Builder(appMap,
               suitableCloudOffersMap, topology).setMutationProbability(0.1)
               .setCrossoverProbability(0.5).setMaxEvaluations(20000).build();
         break;
      default:
         engine = new Random.Builder(appMap, suitableCloudOffersMap, topology)
               .build();
         // TODO:Complete with more algorithms
      }

   }

   public Solution optimize() {

      SolutionSet resultSet = null;
      try {
         resultSet = engine.computeOptimizationProblem();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (JMException e) {
         e.printStackTrace();
      }

      // Epsilon-Constrained
//      Solution bestSolution = resultSet
//            .best(new EpsilonDominanceComparator(EPSILON));
//
//      if (bestSolution.getOverallConstraintViolation() < 0) {
//         throw new IllegalStateException(
//               "The user requirements are too restrictive for the application built\nExceeding in:"
//                     + checkConstraintsExceeding(bestSolution));
//      }
//      
//       String bestSol = "";
//      
//       int numOfVariables = bestSolution.getDecisionVariables().length;
//       for (int i = 0; i < numOfVariables; i++) {
//       bestSol += bestSolution.getDecisionVariables()[i].toString();
//       }
//       log.info("\n\nEpsilonDominanceComparator\nBest Solution found:\n\n" + bestSol);

      
      
      
//       Weighted-Method (now without any weight or equally, each weight=1)
     Solution bestSolution = obtainBestSolution(resultSet);
       
      
       
     if (bestSolution.getOverallConstraintViolation() < 0) {
     throw new IllegalStateException(
           "The user requirements are too restrictive for the application built\nExceeding in:"
                 + checkConstraintsExceeding(bestSolution));
  }
       

      /*********/
      printResultsFiles(resultSet);
      /*********/

      return bestSolution;

   }

   private String checkConstraintsExceeding(Solution nonDomSolution) {
      String constraintsExceeded = "";
      double[] constraints = ((Modules) nonDomSolution
            .getDecisionVariables()[0]).getConstraints();

      for (int i = 0; i < ((Modules) nonDomSolution
            .getDecisionVariables()[0]).getConstraints().length; i++) {
         switch (i) {
         case 0:
            if (constraints[i] > 0) {
               constraintsExceeded += "\nPerformance of: "
                     + ( ((Modules) nonDomSolution.getDecisionVariables()[0])
                           .getGlobalResponseTime() - ((Modules) nonDomSolution.getDecisionVariables()[0])
                           .getRequirements().getResponseTime() )  + " [seconds]";
            }
            break;
         case 1:
            if (constraints[i] > 0) {
               constraintsExceeded += "\nAvailability of: "
                     + ( ((Modules) nonDomSolution.getDecisionVariables()[0])
                           .getGlobalAvailability()*100 - ((Modules) nonDomSolution.getDecisionVariables()[0])
                           .getRequirements().getAvailability()*100 ) + " [%]";
            }
            break;
         case 2:
            if (constraints[i] > 0) {
               constraintsExceeded += "\nCost of: "
                     + String.valueOf( ( ((Modules) nonDomSolution.getDecisionVariables()[0]).getRequirements().getCostMonth() - ((Modules) nonDomSolution.getDecisionVariables()[0]).getGlobalCost()) )
                     + " [€/month]";
            }
            break;
         }
      }
      ((Modules) nonDomSolution.getDecisionVariables()[0])
            .getViolatedConstraints();
      return constraintsExceeded;
   }

   private void printResultsFiles(SolutionSet resultSet) {

      String targetPath = "/src/test/target/";

      String pathforComputedAppRequirements = System.getProperty("user.dir")
            + targetPath + "ComputedVariables_"
            + engine.getAlgorithm().getClass().getSimpleName();

      try {

         FileOutputStream fos = new FileOutputStream(
               pathforComputedAppRequirements);
         OutputStreamWriter osw = new OutputStreamWriter(fos);
         BufferedWriter bw = new BufferedWriter(osw);

         for (int i = 0; i < resultSet.size(); i++) {
            ((Modules) resultSet.get(i).getDecisionVariables()[0])
                  .printComputedGlobalVariables(pathforComputedAppRequirements,
                        bw);
            bw.newLine();
         }
         bw.close();
      } catch (IOException e) {
         Configuration.logger_.severe("Error acceding to the file");
         e.printStackTrace();
      }

      log.info("Objectives values have been writen in '" + targetPath
            + "' to file FUN_"
            + engine.getAlgorithm().getClass().getSimpleName() + "_Single");
      resultSet.printObjectivesToFile(System.getProperty("user.dir")
            + targetPath + "FUN_"
            + engine.getAlgorithm().getClass().getSimpleName() + "_Single");
      log.info("Variables values have been writen in '" + targetPath
            + "' to file VAR_"
            + engine.getAlgorithm().getClass().getSimpleName() + "_Single");
      resultSet.printVariablesToFile(System.getProperty("user.dir")
            + targetPath + "VAR_"
            + engine.getAlgorithm().getClass().getSimpleName() + "_Single");

   }

   private Solution obtainBestSolution(SolutionSet resultSet) {

      Solution bestSolution;

      // If is MultiObjective than find the min(sum(f(objectives[i])))
      if (engine.getProblem().getNumberOfObjectives() > 1) {
         double min = Double.POSITIVE_INFINITY;
         double temp = 0;
         int bestSolutionIndex = 0;
         for (int i = 0; i < resultSet.size(); i++) {
            for (int j = 0; j < resultSet.get(i).getNumberOfObjectives(); j++) {
               temp += resultSet.get(i).getObjective(j);
            }
            if (temp < min) {
               min = temp;
               bestSolutionIndex = i;
            }
            temp = 0;
         }
         bestSolution = resultSet.get(bestSolutionIndex);
      } else { // Single-objective Optimization Problem
         bestSolution = resultSet.get(0);
      }

      return bestSolution;
   }

   // private printCompu

   private void initializeProblem(String appModel, String suitableCloudOffer) {

      // Get app characteristics
      appMap = YAMLoptimizerParserNew.GetMAPofAPP(appModel);
      log.debug("appMap_ read");

      // Get cloud offers
      suitableCloudOffersMap = YAMLoptimizerParserNew
            .getMAPofCloudOffers(suitableCloudOffer);
      log.debug("suitableCloudOffersMap READ");

      topology = YAMLoptimizerParserNew.getApplicationTopology(appMap,
            suitableCloudOffersMap);
      log.debug("topology read");
   }

   private void printFitnessOnConsole(SolutionSet resultSet) {
      String resultObjectivesSetString = "";

      // This could be used for print FUN file
      for (int i = 0; i < resultSet.size(); i++) {
         for (int j = 0; j < resultSet.get(i).getDecisionVariables().length; j++) {
            resultObjectivesSetString += resultSet.get(i).toString();
         }
      }

      System.out.println(resultObjectivesSetString);

   }

   private void printVariableOnConsole(SolutionSet resultSet) {
      String resultVariablesSetString = "";

      // This could be used for print VAR file
      for (int i = 0; i < resultSet.size(); i++) {
         for (int j = 0; j < resultSet.get(i).getDecisionVariables().length; j++) {
            resultVariablesSetString += resultSet.get(i).getDecisionVariables()[j]
                  .toString();
         }
      }

      System.out.println(resultVariablesSetString);

   }

}
