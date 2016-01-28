package eu.seaclouds.platform.planner.optimizer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.util.JMException;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.singleobjective.AsynchronousCellularGASearch;
import eu.seaclouds.platform.planner.optimizer.nfp.QualityInformation;
import eu.seaclouds.platform.planner.optimizer.nfp.SeaCloudsQualityAnalyzerNew;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

public class SeaCloudsTypeSpecializationNotUsed extends SolutionType {

   private Map<String, Object> appMap;
   private Map<String, Object> suitableCloudOffersMap;
   private Topology topology;
   private List<Module> moduleList;
   private MetaHeuristic engine;
   private boolean needSpecialInitialization;
   private QualityInformation requirements;
   private SeaCloudsQualityAnalyzerNew qualityAnalyzer;

   public SeaCloudsTypeSpecializationNotUsed(Problem problem) {
      super(problem);
   }

   public SeaCloudsTypeSpecializationNotUsed(Problem problem, Map<String, Object> appMap,
         Map<String, Object> allCloudOffersMap, Topology topology, QualityInformation requirements, SeaCloudsQualityAnalyzerNew qualityAnalyzer) {

      super(problem);

      this.appMap = appMap;
      this.suitableCloudOffersMap = allCloudOffersMap;
      this.topology = topology;
      this.requirements = requirements;
      this.qualityAnalyzer = qualityAnalyzer;
      this.needSpecialInitialization = true;
//      this.moduleList = YAMLoptimizerParserNew
//            .getModuleListWithCharacteristics(appMap, allCloudOffersMap);

   }

   // For Testing
   public SeaCloudsTypeSpecializationNotUsed(Problem problem, List<Module> moduleList,
         Topology topology) {
      super(problem);

      this.topology = topology;
      this.moduleList = moduleList;

   }

   public SeaCloudsTypeSpecializationNotUsed(Problem problem, Map<String, Object> appMap,
         Map<String, Object> allCloudOffersMap, Topology topology, QualityInformation requirements, SeaCloudsQualityAnalyzerNew qualityAnalyzer, boolean needSpecialInitialization) {
      
      super(problem);

      this.appMap = appMap;
      this.suitableCloudOffersMap = allCloudOffersMap;
      this.topology = topology;
      this.requirements = requirements;
      this.qualityAnalyzer = qualityAnalyzer;
      this.needSpecialInitialization = needSpecialInitialization;
      
   }

   @Override
   public Variable[] createVariables() throws ClassNotFoundException {

      Variable[] variables = new Variable[problem_.getNumberOfVariables()];

      

      if(false){
         Solution bestTempSolution = null;
         try {
            engine = new AsynchronousCellularGASearch.Builder(appMap,
                  suitableCloudOffersMap, topology).setMutationProbability(0.1)
                  .setCrossoverProbability(0.5).setPopulationSize(400)
                  .setMaxEvaluations(1000).build();
            SolutionSet resultSet = engine.computeOptimizationProblem();
            
            bestTempSolution = obtainBestSolution(resultSet);
         } catch (JMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
//         int numOfVariables = bestTempSolution.getDecisionVariables().length;
         //If there will be other variable the following row should change and loop it in a For
//         int numOfModules = ((Modules)bestTempSolution.getDecisionVariables()[0]).getModuleList().size();
         
         List<Module> newModuleList = ((Modules)bestTempSolution.getDecisionVariables()[0]).getModuleList();
         
         Modules modules = new Modules(problem_.getNumberOfConstraints(), problem_,
               newModuleList, topology, requirements, qualityAnalyzer);
         
         variables[0] = modules.deepCopy();
      }
      else{
         
      // Generate a new list of modules as a Variable ready to be evaluated for a New Solution.
         this.moduleList = YAMLoptimizerParserNew
               .getModuleListWithCharacteristics(appMap, suitableCloudOffersMap);
         
         variables[0] = new Modules(problem_.getNumberOfConstraints(),
               problem_, moduleList, topology, requirements, qualityAnalyzer);
      }
      


      return variables;

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

}
