package eu.seaclouds.platform.planner.optimizer.problem;

import java.util.Map;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Modules;
import eu.seaclouds.platform.planner.optimizer.SeaCloudsType;
import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.nfp.QualityInformation;
import eu.seaclouds.platform.planner.optimizer.nfp.SeaCloudsQualityAnalyzerNew;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

@SuppressWarnings("serial")
public class SeaCloudsProblemSingleObj extends Problem {

   static Logger log = LoggerFactory.getLogger(SeaCloudsProblemSingleObj.class);
   private static final boolean DEBUG = true;
   private int perfCount;
   private int avaCount;
   private int costCount;
   private Map<String, Object> allCloudOffersMap;
   private Topology topology;
   private QualityInformation requirements;
   private SeaCloudsQualityAnalyzerNew qualityAnalyzer;
   private int numberOfQoStoCompute;

   private QualityInformation loadQualityRequirements(Map<String, Object> appMap) {
      
      QualityInformation requirements = YAMLoptimizerParserNew
            .getQualityRequirements(appMap);
      
      if (!requirements.hasValidWorkload()
              && requirements.existResponseTimeRequirement()) {
          loadWorkload(requirements, appMap);
      }

      return requirements;
      
  }
  
  private QualityInformation loadWorkload(QualityInformation requirements, Map<String, Object> appMap) {
     if (requirements.getWorkload() <= 0.0) {
         // requirements.setWorkloadMinute(YAMLoptimizerParserNew
         // .getApplicationWorkload(Parameters.getAppMap()));
         requirements.setWorkloadMinute(YAMLoptimizerParserNew
                 .getApplicationWorkload(appMap));
     }
     // Maybe the previous operation did not work correctly because the
     // workload could not be found in the YAML. Follow an ad-hoc solution to
     // get some requirements
     if (!requirements.hasValidWorkload()) {
         log.error("Valid workload information not found in the input document. Loading dummy quality requirements for testing purposes");
         requirements.setWorkloadMinute(YAMLoptimizerParserNew
                 .getApplicationWorkloadTest());
     }
     
     return requirements;

 }
   
  public SeaCloudsProblemSingleObj(Map<String, Object> appMap,
        Map<String, Object> allCloudOffersMap, Topology topology) {
     
     this.allCloudOffersMap = allCloudOffersMap;
     this.topology = topology;
    
     numberOfObjectives_ = 1;
     numberOfQoStoCompute = 3;
     numberOfConstraints_ = numberOfQoStoCompute;
     numberOfVariables_ = 1;
    
     problemName_ = "SeaCloudProblemSO";
     
     requirements = loadQualityRequirements(appMap);
     
     qualityAnalyzer = new SeaCloudsQualityAnalyzerNew(
           allCloudOffersMap);
     
//     solutionType_ = new SeaCloudsTypeNewModules(this, modules, topology);
   solutionType_ = new SeaCloudsType(this, appMap, allCloudOffersMap, topology, requirements, qualityAnalyzer);
    }
  
//   public SeaCloudsProblemSingleObj(Map<String, Object> appMap,
//         Map<String, Object> allCloudOffersMap, Topology topology,
//      boolean needSpecialInitialization) {
//
//      
//      this.allCloudOffersMap = allCloudOffersMap;
//      this.topology = topology;
//     
//      numberOfObjectives_ = 1;
//      numberOfQoStoCompute = 3;
//      numberOfConstraints_ = numberOfQoStoCompute;
//      numberOfVariables_ = 1;
//     
//      problemName_ = "SeaCloudProblemSO";
//      
//      requirements = loadQualityRequirements(appMap);
//      
//      qualityAnalyzer = new SeaCloudsQualityAnalyzerNew(
//            allCloudOffersMap);
//      
////      solutionType_ = new SeaCloudsTypeNewModules(this, modules, topology);
//    solutionType_ = new SeaCloudsType(this, appMap, allCloudOffersMap, topology, requirements, qualityAnalyzer, needSpecialInitialization);
//      
//}

   @Override
   public void evaluate(Solution solution) throws JMException {

      Modules variable = ((Modules) solution.getDecisionVariables()[0]);
      // variable.computeObjectiveFunctions();
//      variable.computeObjectiveFunctions(this.requirements, this.allCloudOffersMap, this.topology);

//      double[] obj = new double[numberOfObjectives_];
//      double globalFitness = 0;
//      
//      for (int i = 0; i < numberOfObjectives_; i++) {
//         obj[i] = variable.getObjectiveFunctions(i);
//         solution.setObjective(i, obj[i]);
//         globalFitness+=obj[i];
//      }
      
      
//      double[] obj = new double[numberOfConstraints_];
//      double globalFitness = 0;
//      
//      for (int i = 0; i < numberOfConstraints_; i++) {
//         variable.computeGlobalQoS(i, requirements, qualityAnalyzer);
//         obj[i] = variable.getObjectiveFunction(i);
//         globalFitness+=obj[i];
//      }
//      
//      variable.setGlobalFitness(globalFitness);
//      solution.setObjective(0, globalFitness);
      
      
//      variable.computeGlobalQoS(requirements, qualityAnalyzer);
      
      double[] obj = new double[numberOfQoStoCompute];
      double globalFitness = 0;
      
      for (int i = 0; i < numberOfQoStoCompute; i++) {
         
         
         switch (i) {
         case 0:
//            obj[i] = variable.getGlobalResponseTime();
            obj[i] = variable.getObjectiveFunctions(i);
            break;
            
         case 1:
//            obj[i] = -variable.getGlobalAvailability();
            obj[i] = variable.getObjectiveFunctions(i);
            break;
            
         case 2:
//            obj[i] = variable.getGlobalCost();
            obj[i] = variable.getObjectiveFunctions(i);
            break;
         }
         globalFitness+=obj[i];
      }

      //Locally Saving Fitness for visualization of VAR file
      variable.setGlobalFitness(globalFitness);
      
      solution.setObjective(0, globalFitness);
   }

   @Override
   public void evaluateConstraints(Solution solution) throws JMException {

Modules variable = ((Modules) solution.getDecisionVariables()[0]);
      
      double[] constraints = variable.getConstraints();

      double total = 0.0;
      int number = 0;

      for (int i = 0; i < numberOfConstraints_; i++) {
         switch (i) {
         case 0:
            if (constraints[i] > 0.0) {
               if (DEBUG) {
                  log.debug("-------Vincolo di PERFORMANCE non soddisfatto di: "
                        + Math.abs(constraints[i]) + "-------");
                  
               }
               perfCount++;
               variable.setPerformanceViolationCount(perfCount);
               variable.setViolatedConstraints(-constraints[i], i);
               total += -constraints[i];
               number++;
            } else {
               if (DEBUG) {
                  // log.debug("Vincolo di PERFORMANCE soddisfatto di: "
                  // + Math.abs(constraint[i]));
                  log.debug("Vincolo di PERFORMANCE soddisfatto di: "
                        + constraints[i]);
               }
            }
            break;
         case 1:
            if (constraints[i] > 0.0) {

               if (DEBUG) {
                  log.debug("-------Vincolo di AVAILABILITY non soddisfatto di: "
                        + constraints[i] + "-------");
                  
               }
               avaCount++;
               variable.setAvailabilityViolationCount(perfCount);
               variable.setViolatedConstraints(-constraints[i], i);
               total += (-constraints[i]);
               number++;
            } else {
               if (DEBUG) {
                  log.debug("Vincolo di AVAILABILITY soddisfatto di: "
                        + constraints[i]);
               }
            }
            break;
         case 2:
            if (constraints[i] > 0.0) {

               if (DEBUG) {
                  log.debug("-------Vincolo di COST non soddisfatto di: "
                        + Math.abs(constraints[i]) + "-------");
                  
               }
               costCount++;
               variable.setCostViolationCount(perfCount);
               variable.setViolatedConstraints(-constraints[i], i);
               total += -constraints[i];
               number++;
            } else {
               if (DEBUG) {
                  log.debug("Vincolo di COST soddisfatto di: " + constraints[i]);
               }
            }
            break;
         }
      }

      solution.setOverallConstraintViolation(total);
      solution.setNumberOfViolatedConstraint(number);

   }

}
