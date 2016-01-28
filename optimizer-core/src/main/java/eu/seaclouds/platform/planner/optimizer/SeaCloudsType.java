package eu.seaclouds.platform.planner.optimizer;

import java.util.List;
import java.util.Map;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import eu.seaclouds.platform.planner.optimizer.nfp.QualityInformation;
import eu.seaclouds.platform.planner.optimizer.nfp.SeaCloudsQualityAnalyzerNew;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

public class SeaCloudsType extends SolutionType {

   private Map<String, Object> appMap;
   private Map<String, Object> suitableCloudOffersMap;
   private Topology topology;
   private List<Module> moduleList;
   private QualityInformation requirements;
   private SeaCloudsQualityAnalyzerNew qualityAnalyzer;

   public SeaCloudsType(Problem problem) {
      super(problem);
   }

   public SeaCloudsType(Problem problem, Map<String, Object> appMap,
         Map<String, Object> allCloudOffersMap, Topology topology, QualityInformation requirements, SeaCloudsQualityAnalyzerNew qualityAnalyzer) {

      super(problem);

      this.appMap = appMap;
      this.suitableCloudOffersMap = allCloudOffersMap;
      this.topology = topology;
      this.requirements = requirements;
      this.qualityAnalyzer = qualityAnalyzer;

   }

   // For Testing
   public SeaCloudsType(Problem problem, List<Module> moduleList,
         Topology topology) {
      super(problem);

      this.topology = topology;
      this.moduleList = moduleList;

   }

   @Override
   public Variable[] createVariables() throws ClassNotFoundException {

      Variable[] variables = new Variable[problem_.getNumberOfVariables()];
      
      // Generate a new list of modules as a Variable ready to be evaluated for a New Solution.
         this.moduleList = YAMLoptimizerParserNew
               .getModuleListWithCharacteristics(appMap, suitableCloudOffersMap);
         
         variables[0] = new Modules(problem_.getNumberOfConstraints(),
               problem_, moduleList, topology, requirements, qualityAnalyzer);

      return variables;

   }

}
