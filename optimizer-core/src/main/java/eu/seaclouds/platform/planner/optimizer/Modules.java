//  ArrayReal.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
// 
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package eu.seaclouds.platform.planner.optimizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.util.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.nfp.QualityInformation;
import eu.seaclouds.platform.planner.optimizer.nfp.SeaCloudsQualityAnalyzerNew;

/**
 * Class implementing a decision encodings.variable representing an array of
 * real values. The real values of the array have their own bounds.
 */
public class Modules extends Variable implements Cloneable {

//   private static final double MAX_TIMES_IMPROVE_REQUIREMENT_PERF = 10;
//   private static final double MAX_TIMES_IMPROVE_REQUIREMENT_AVA = 10;
//   private static final double MAX_TIMES_IMPROVE_REQUIREMENT_COST = 10;
   
//   private double MAX_TIMES_IMPROVE_REQUIREMENT = 30;
   
   private double MAX_TIMES_IMPROVE_REQUIREMENT_PERF = 10;
   private double MAX_TIMES_IMPROVE_REQUIREMENT_AVA = 10;
   private double MAX_TIMES_IMPROVE_REQUIREMENT_COST = 10;

//   private static final double MAX_AVAILABILITY_PERMITTED = 0.9999999999999999;

   /**
    * Problem used
    */
   private Problem seaCloudsProblem;

   private List<Module> moduleList;

   // private List<Module> computableModuleList;

   private double globalFitness;

   private double[] constraints;

   private double[] violatedConstraints;

   private Topology topology;

   /**
    * Stores the final @Performance value of the application, calculated for
    * each QoS requirement from the Maps provided
    */
   private double globalResponseTime;

   /**
    * Stores the final @Availbility value of the application, calculated for
    * each QoS requirement from the Maps provided
    */
   private double globalAvailability;

   /**
    * Stores the final @Cost value of the application, calculated for each QoS
    * requirement from the Maps provided
    */
   private double globalCost;

   private QualityInformation requirements;

   private SeaCloudsQualityAnalyzerNew qualityAnalyzer;

//   private double maximumDifferenceOfNumOfDecimalForAvailability;

   private double[] objectiveFunctions;

   private int performanceViolationCount;

   private int availabilityViolationCount;

   private int costViolationCount;

   static Logger log = LoggerFactory.getLogger(Modules.class);

   public Problem getSeaCloudsProblem() {
      return seaCloudsProblem;
   }

   public void setSeaCloudsProblem(Problem seaCloudsProblem) {
      this.seaCloudsProblem = seaCloudsProblem;
   }

   public QualityInformation getRequirements() {
      return requirements;
   }

   public void setRequirements(QualityInformation requirements) {
      this.requirements = requirements;
   }

   public SeaCloudsQualityAnalyzerNew getQualityAnalyzer() {
      return qualityAnalyzer;
   }

   public void setQualityAnalyzer(SeaCloudsQualityAnalyzerNew qualityAnalyzer) {
      this.qualityAnalyzer = qualityAnalyzer;
   }

   public void setViolatedConstraints(double[] violatedConstraints) {
      this.violatedConstraints = violatedConstraints;
   }

   public List<Module> getModuleList() {
      return moduleList;
   }

   public void setModuleList(List<Module> moduleList) {
      this.moduleList = moduleList;
   }

   // public List<Module> getComputableModuleList() {
   // return computableModuleList;
   // }
   //
   // public void setComputableModuleList(List<Module> computableModuleList) {
   // this.computableModuleList = computableModuleList;
   // }

   public double getObjectiveFunctions(int index) {
      return objectiveFunctions[index];
   }

   public void setObjectiveFunctions(double[] objectiveFunctions) {
      this.objectiveFunctions = objectiveFunctions;
   }

   public double getGlobalFitness() {
      return globalFitness;
   }

   public void setGlobalFitness(double globalFitness) {
      this.globalFitness = globalFitness;
   }

   public double getGlobalResponseTime() {
      return globalResponseTime;
   }

   public void setGlobalResponseTime(double globalResponseTime) {
      this.globalResponseTime = globalResponseTime;
   }

   public double getGlobalAvailability() {
      return globalAvailability;
   }

   public void setGlobalAvailability(double globalAvailability) {
      this.globalAvailability = globalAvailability;
   }

   public double getGlobalCost() {
      return globalCost;
   }

   public void setGlobalCost(double globalCost) {
      this.globalCost = globalCost;
   }

   public double[] getConstraints() {
      return constraints;
   }

   public void setConstraints(double[] constraints) {
      this.constraints = constraints;
   }
   
   public int getPerformanceViolationCount(){
      return performanceViolationCount;
   }
   
   public void setPerformanceViolationCount(int perfCount){
      this.performanceViolationCount = perfCount;
   }
   
   public int getAvailabilityViolationCount(){
      return availabilityViolationCount;
   }
   
   public void setAvailabilityViolationCount(int availabilityCount){
      this.availabilityViolationCount = availabilityCount;
   }
   
   public int getCostViolationCount(){
      return costViolationCount;
   }
   
   public void setCostViolationCount(int costCount){
      this.costViolationCount = costCount;
   }

   public Topology getTopology() {
      return topology;
   }

   public void setTopology(Topology topology) {
      this.topology = topology;
   }

   public void setViolatedConstraints(double constraint, int index) {
      this.violatedConstraints[index] = constraint;
   }

   public double[] getViolatedConstraints() {
      return violatedConstraints;
   }

   //
   // //WITHOUT LoadQualityRequirements
   // /**
   // * Constructor
   // *
   // */
   // public Modules(int numOfRequirements, Problem problem, Map<String, Object>
   // appMap,
   // Map<String, Object> allCloudOffersMAP, Topology topology) {
   // this.seaCloudsProblem = problem;
   // this.appMap = appMap;
   // this.allCloudOffersMAP = allCloudOffersMAP;
   // this.topology = topology;
   //
   // moduleList = YAMLoptimizerParserNew.getModuleListWithCharacteristics(
   // appMap, allCloudOffersMAP);
   //
   // // numOfRequirements correspond to numberOfVariables: Performance,
   // Availability, Cost
   // this.numOfRequirements = numOfRequirements;
   // applicationRequirements = new double[numOfRequirements];
   // objectiveFunctions = new double[numOfRequirements];
   //
   // // loadQualityRequirements();
   //
   // // computeObjectiveFunctions();
   //
   // } // Constructor
   //

   public Modules(int numOfRequirements, Problem problem,
         List<Module> moduleList, Topology topology,
         QualityInformation requirements,
         SeaCloudsQualityAnalyzerNew qualityAnalyzer) {
      this.seaCloudsProblem = problem;
      this.topology = topology;
      this.moduleList = moduleList;
      this.requirements = requirements;
      this.qualityAnalyzer = qualityAnalyzer;

      this.objectiveFunctions = new double[numOfRequirements];
      
      
      computeGlobalQoS();
      computeConstraints(numOfRequirements);

   } // Constructor

   private List<Module> getModuleComputableList(List<Module> modList) {
      List<Module> modCompList = new ArrayList<Module>();
      for (Module m : modList) {
         if (m.isComputable()) {
            modCompList.add(m);
         }
      }
      return modCompList;
   }

   public static List<Module> cloneList(List<Module> list)
         throws CloneNotSupportedException {
      List<Module> clone = new ArrayList<Module>(list.size());
      for (Module item : list)
         clone.add(item.clone());
      return clone;
   }

   /**
    * Copy Constructor
    *
    * @param arrayReal
    *           The arrayReal to copy
    */
   private Modules(Modules modules) {

      try {
         this.seaCloudsProblem = modules.seaCloudsProblem;
         this.moduleList = cloneList(modules.moduleList);
         this.topology = modules.topology; // Read only. Otherwise explain why
                                           // you need to modify it
         this.objectiveFunctions = modules.objectiveFunctions.clone();
         this.globalResponseTime = modules.globalResponseTime;
         this.globalAvailability = modules.globalAvailability;
         this.globalCost = modules.globalCost;
         this.globalFitness = modules.globalFitness;

         this.requirements = modules.requirements;
         this.qualityAnalyzer = modules.qualityAnalyzer;

         try {
            this.constraints = modules.constraints.clone();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

         this.violatedConstraints = modules.violatedConstraints.clone();

      } catch (CloneNotSupportedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   } // Copy Constructor

   @Override
   public Variable deepCopy() {
      return new Modules(this);
   } // deepCopy

   /**
    * Returns the numberOfVariables.
    *
    * @return The length
    */
   public int getLength() {
      return moduleList.size();
   } // getLength

   public void computeGlobalQoS() {
      
    //Getting Variable Constraints
      double performanceConstraint = requirements.getResponseTime();
      double availabilityContraint = requirements.getAvailability();
      double costConstraint = requirements.getCostHour();
      
      
//      double performanceAdjustment;
//      double availabilityAdjustment;
//      double costAdjustment;
//      if((3+Math.abs(Math.log10(1/performanceConstraint)))!=1){
//         performanceAdjustment = 3+Math.abs(Math.log10(1/performanceConstraint));
//      }
//      else{
//         performanceAdjustment = 1;
//      }
//      if(Math.abs(Math.log10(1-availabilityContraint))!=1){
//         availabilityAdjustment = Math.abs(Math.log10(1-availabilityContraint));
//      }
//      else{
//         availabilityAdjustment = 1;
//      }
//      if((3+Math.abs(Math.log10(1/costConstraint)))!=1){
//         costAdjustment = 3+Math.abs(Math.log10(1/costConstraint));
//      }
//      else{
//         costAdjustment = 1;
//      }
//      
//      double adjustment = performanceAdjustment + availabilityAdjustment + costAdjustment;
      
   // evaluation of Response Time
      globalResponseTime = computeGlobalResponseTime();
      // evaluation of Performance
      double performance = - performanceConstraint / globalResponseTime;

   // evaluation of Availability
      globalAvailability = computeGlobalAvailability();
      double availability = - (1 - availabilityContraint)
            / (1 - globalAvailability);
      
   // evaluation of Cost
      globalCost = computeGlobalCost();
      double cost = - costConstraint / globalCost;
      
//      MAX_TIMES_IMPROVE_REQUIREMENT_PERF = performanceConstraint;
//      MAX_TIMES_IMPROVE_REQUIREMENT_AVA = availabilityContraint;
//      MAX_TIMES_IMPROVE_REQUIREMENT_COST = costConstraint;
      
//      objectiveFunctions[0] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_PERF, performance*performanceAdjustment/adjustment);
//      objectiveFunctions[1] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_AVA, availability*availabilityAdjustment/adjustment);
//      objectiveFunctions[2] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_COST, cost*costAdjustment/adjustment);
      
      objectiveFunctions[0] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_PERF, performance);
      objectiveFunctions[1] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_AVA, availability);
      objectiveFunctions[2] = Math.max(-MAX_TIMES_IMPROVE_REQUIREMENT_COST, cost);
      
   }

   public void computeConstraints(int numOfConstraints) {

      constraints = new double[numOfConstraints];
      violatedConstraints = new double[numOfConstraints];

      // Getting Variable Constraints
      double performanceConstraint = requirements.getResponseTime();
      double availabilityContraint = requirements.getAvailability();
      double costConstraint = requirements.getCostHour();
      

      double performance = globalResponseTime / performanceConstraint;
      double availability = (1 - globalAvailability)
            / (1 - availabilityContraint);
      double cost = globalCost / costConstraint;

      if(performance>=1){//vincolo non soddisfatto
         this.constraints[0] = Math.min(MAX_TIMES_IMPROVE_REQUIREMENT_PERF, performance)-1;
      }
      else{
         this.constraints[0] = performance-1;
      }
      if(availability>=1){//vincolo non soddisfatto
         this.constraints[1] = Math.min(MAX_TIMES_IMPROVE_REQUIREMENT_AVA, availability)-1;
      }
      else{
         this.constraints[1] = availability-1;
      }
      if(cost>=1){//vincolo non soddisfatto
         this.constraints[2] = Math.min(MAX_TIMES_IMPROVE_REQUIREMENT_COST, cost)-1;
      }
      else{
         this.constraints[2] = cost-1;
      }
      
      
      
      
//      this.constraints[0] = performanceConstraint - globalResponseTime;
//      this.constraints[1] = Math
//      .log10(1 - availabilityContraint)
//      - Math.log10(1 - globalAvailability);
//      this.constraints[2] = costConstraint - globalCost;

   }

   // private double computeIthRequirement(int index, QualityInformation
   // requirements, Map<String, Object> allCloudOffersMap, Topology topology) {
   //
   // SeaCloudsQualityAnalyzerNew qualityAnalyzer = new
   // SeaCloudsQualityAnalyzerNew(
   // allCloudOffersMap);
   //
   // switch (index) {
   // case 0:
   // return computePerformance(requirements, qualityAnalyzer, topology);
   // case 1:
   // return computeAvailability(requirements, qualityAnalyzer, topology);
   // case 2:
   // return computeCost(requirements, qualityAnalyzer);
   // default:
   // return 0;
   // }
   //
   // }
   //
   // private void loadQualityRequirements(Map<String, Object> appMap) {
   // if (requirements == null) {
   // // requirements = YAMLoptimizerParserNew
   // // .getQualityRequirements(Parameters.getAppMap());
   // requirements = YAMLoptimizerParserNew
   // .getQualityRequirements(appMap);
   // }
   // // Maybe the previous operation did not work because Requirements could
   // // not be found in the YAML. Follow an ad-hoc solution to get some
   // // requirements
   // if (requirements == null) {
   // log.error("Quality requirements not found in the input document. Loading dummy quality requirements for testing purposes");
   // requirements = YAMLoptimizerParserNew
   // .getQualityRequirementsForTesting();
   //
   // }
   //
   // // if(requirements.existResponseTimeRequirement()){
   // // loadWorkload();
   // // }
   //
   // if (!requirements.hasValidWorkload()
   // && requirements.existResponseTimeRequirement()) {
   // loadWorkload(appMap);
   // }
   //
   // }
   //
   // private void loadWorkload(Map<String, Object> appMap) {
   // if (requirements.getWorkload() <= 0.0) {
   // // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // // .getApplicationWorkload(Parameters.getAppMap()));
   // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // .getApplicationWorkload(appMap));
   // }
   // // Maybe the previous operation did not work correctly because the
   // // workload could not be found in the YAML. Follow an ad-hoc solution to
   // // get some requirements
   // if (!requirements.hasValidWorkload()) {
   // log.error("Valid workload information not found in the input document. Loading dummy quality requirements for testing purposes");
   // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // .getApplicationWorkloadTest());
   // }
   //
   // }

   private double computeGlobalResponseTime() {

      // calculates how well it satisfies performance reuquirement. Method
      // computePerformance returns a structure because, beyond response time
      // information, other performance-related information can be useful for
      // guiding the search method towards better solutions

      if (requirements.existResponseTimeRequirement()) {
         // return qualityAnalyzer.computePerformance(moduleList,
         // Parameters.getTopology(), requirements.getWorkload());
         return qualityAnalyzer.computePerformance(moduleList, topology,
               requirements.getWorkload());
      }
      return 1;
   }

   private double computeGlobalAvailability() {
      // calculates how well it satisfies availability reuquirement, if it
      // exists

      if (requirements.existAvailabilityRequirement()) {
         return qualityAnalyzer.computeAvailability(moduleList, topology);
      }
      return 1;
   }

   private double computeGlobalCost() {

      if (requirements.existCostRequirement()) {
         return qualityAnalyzer.computeCost(moduleList);
      }
      return 1;
   }

   //
   // public void computeObjectiveFunctions() {
   // for (int i = 0; i < size; i++) {
   // applicationRequirements[i] = computeIthRequirement(i, moduleList);
   // }
   //
   // // Getting Variable Constraints
   // double performanceConstraint = requirements.getResponseTime();
   // double availabilityContraint = requirements.getAvailability();
   // double costConstraint = requirements.getCostHour();
   //
   // // evaluation of Performance
   // double performance = performanceConstraint / applicationRequirements[0];
   //
   // // evaluation of Availability
   // double availability = (1 - availabilityContraint)
   // / (1 - applicationRequirements[1]);
   //
   // // evaluation of Cost
   // double cost = costConstraint / applicationRequirements[2];
   //
   // // final fitness function for 1 Evaluation (multiplied by -1 because is
   // // maximization problem converted in minimization one)
   // objectiveFunctions[0] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT,
   // performance);
   // objectiveFunctions[1] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT,
   // availability);
   // objectiveFunctions[2] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT, cost);
   //
   // globalFitness = objectiveFunctions[0] + objectiveFunctions[1] +
   // objectiveFunctions[2];
   // }
   //
   // public void computeConstraints(int numOfConstraints) {
   //
   // constraints = new double[numOfConstraints];
   // violatedConstraints = new double[numOfConstraints];
   //
   // // Getting Variable Constraints
   // double performanceConstraint = requirements.getResponseTime();
   // double availabilityContraint = requirements.getAvailability();
   // double costConstraint = requirements.getCostHour();
   //
   // // constraints[0] = getComputedValue(0) / performanceConstraint - 1;
   // // constraints[1] = (1 - getComputedValue(1)) / (1 -
   // availabilityContraint) - 1;
   // // constraints[2] = getComputedValue(2) / costConstraint - 1;
   //
   // constraints[0] = applicationRequirements[0] / performanceConstraint - 1;
   // constraints[1] = (1 - applicationRequirements[1]) / (1 -
   // availabilityContraint) - 1;
   // constraints[2] = applicationRequirements[2] / costConstraint - 1;
   //
   // }
   //
   // public void computeObjectiveFunctions() {
   // for (int i = 0; i < size; i++) {
   // applicationRequirements[i] = computeIthRequirement(i, moduleList);
   // }
   //
   // // Getting Variable Constraints
   // double performanceConstraint = requirements.getResponseTime();
   // double availabilityContraint = requirements.getAvailability();
   // double costConstraint = requirements.getCostHour();
   //
   // // evaluation of Performance
   // double performance = performanceConstraint / applicationRequirements[0];
   //
   // // evaluation of Availability
   // double availability = (1 - availabilityContraint)
   // / (1 - applicationRequirements[1]);
   //
   // // evaluation of Cost
   // double cost = costConstraint / applicationRequirements[2];
   //
   // // final fitness function for 1 Evaluation (multiplied by -1 because is
   // // maximization problem converted in minimization one)
   // objectiveFunctions[0] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT,
   // performance);
   // objectiveFunctions[1] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT,
   // availability);
   // objectiveFunctions[2] = -Math.min(MAX_TIMES_IMPROVE_REQUIREMENT, cost);
   //
   // globalFitness = objectiveFunctions[0] + objectiveFunctions[1] +
   // objectiveFunctions[2];
   // }
   //
   // private double computeIthRequirement(int index, List<Module> moduleList) {
   //
   // SeaCloudsQualityAnalyzerNew qualityAnalyzer = new
   // SeaCloudsQualityAnalyzerNew(
   // allCloudOffersMAP);
   //
   // switch (index) {
   // case 0:
   // return computePerformance(qualityAnalyzer, moduleList);
   // case 1:
   // return computeAvailability(qualityAnalyzer, moduleList);
   // case 2:
   // return computeCost(qualityAnalyzer, moduleList);
   // default:
   // return 0;
   // }
   //
   // }
   //
   //
   // private double computePerformance(
   // SeaCloudsQualityAnalyzerNew qualityAnalyzer, List<Module> moduleList) {
   //
   // // calculates how well it satisfies performance reuquirement. Method
   // // computePerformance returns a structure because, beyond response time
   // // information, other performance-related information can be useful for
   // // guiding the search method towards better solutions
   //
   // if (requirements.existResponseTimeRequirement()) {
   // // return qualityAnalyzer.computePerformance(moduleList,
   // // Parameters.getTopology(), requirements.getWorkload());
   // return qualityAnalyzer.computePerformance(moduleList, topology,
   // requirements.getWorkload());
   // }
   // return 1;
   // }
   //
   // private double computeAvailability(
   // SeaCloudsQualityAnalyzerNew qualityAnalyzer, List<Module> moduleList) {
   // // calculates how well it satisfies availability reuquirement, if it
   // // exists
   //
   // if (requirements.existAvailabilityRequirement()) {
   // return qualityAnalyzer.computeAvailability(moduleList, topology);
   // }
   // return 1;
   // }
   //
   // private double computeCost(SeaCloudsQualityAnalyzerNew qualityAnalyzer,
   // List<Module> moduleList) {
   //
   // if (requirements.existCostRequirement()) {
   // return qualityAnalyzer.computeCost(moduleList);
   // }
   // return 1;
   // }
   //
   //
   // private void loadQualityRequirements() {
   // if (requirements == null) {
   // // requirements = YAMLoptimizerParserNew
   // // .getQualityRequirements(Parameters.getAppMap());
   // requirements = YAMLoptimizerParserNew
   // .getQualityRequirements(appMap);
   // }
   // // Maybe the previous operation did not work because Requirements could
   // // not be found in the YAML. Follow an ad-hoc solution to get some
   // // requirements
   // if (requirements == null) {
   // log.error("Quality requirements not found in the input document. Loading dummy quality requirements for testing purposes");
   // requirements = YAMLoptimizerParserNew
   // .getQualityRequirementsForTesting();
   //
   // }
   //
   // // if(requirements.existResponseTimeRequirement()){
   // // loadWorkload();
   // // }
   //
   // if (!requirements.hasValidWorkload()
   // && requirements.existResponseTimeRequirement()) {
   // loadWorkload();
   // }
   //
   // }
   //
   // private void loadWorkload() {
   // if (requirements.getWorkload() <= 0.0) {
   // // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // // .getApplicationWorkload(Parameters.getAppMap()));
   // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // .getApplicationWorkload(appMap));
   // }
   // // Maybe the previous operation did not work correctly because the
   // // workload could not be found in the YAML. Follow an ad-hoc solution to
   // // get some requirements
   // if (!requirements.hasValidWorkload()) {
   // log.error("Valid workload information not found in the input document. Loading dummy quality requirements for testing purposes");
   // requirements.setWorkloadMinute(YAMLoptimizerParserNew
   // .getApplicationWorkloadTest());
   // }
   //
   // }
   //

   public void printComputedGlobalVariables(String path, BufferedWriter bw) {
      try {

         for (int index = 0; index < objectiveFunctions.length; index++) {
            switch (index) {
            case 0:
               bw.write(getGlobalResponseTime() + " ");
               break;
            case 1:
               bw.write(getGlobalAvailability() + " ");
               break;
            case 2:
               bw.write(getGlobalCost() + "");
               break;
            }
         }

      } catch (IOException e) {
         Configuration.logger_.severe("Error acceding to the file");
         e.printStackTrace();
      }
   }

   /**
    * Returns a string representing the object
    *
    * @return The string
    */
   @Override
   public String toString() {
      String string = "";

      string += "Global fitness: " + getGlobalFitness()
            + "\n\tResponse Time: " + getGlobalResponseTime()
            + " [s]"
            + "\n\tAvailability: " + getGlobalAvailability()
            + "\t" + getGlobalAvailability()*100
            + " [%]"
            + "\n\tCost: " + getGlobalCost()
            + " [€/h]"
            + "\t\t" + getGlobalCost()*24*30
            + " [€/month]"
            + "\n\n";

      string += "Violating constrains:\n";
      for (int i = 0; i < violatedConstraints.length; i++) {
         switch (i) {
         case 0:
            string += "\tResponse Time: "
                  + (violatedConstraints[i] == 0 ? "FALSE" : "TRUE: "
                        + constraints[i]) + "\n";
            break;
         case 1:
            string += "\tAvailability: "
                  + (violatedConstraints[i] == 0 ? "FALSE" : "TRUE: "
                        + constraints[i]) + "\n";
            break;
         case 2:
            string += "\tCost: "
                  + (violatedConstraints[i] == 0 ? "FALSE" : "TRUE: "
                        + constraints[i]) + "\n";
            break;
         }

      }

      string += "\nList of Modules:\n";

      for (Module m : moduleList) {
         if (m.isComputable()) {
            string += m.getName() + ":\n" + "\tBestCloudOffer: "
                  + m.getNumOfInstancesForBestCloudOffer() + " instances of "
                  + m.getBestCloudOffer().getName() + "\n"
                  + "\t\tPerformance: "
                  + m.getBestCloudOffer().getPerformance()
                  + "\n\t\tAvailability: "
                  + m.getBestCloudOffer().getAvailability()
                  + "\n\t\tCost [€/h]: " + m.getBestCloudOffer().getCost()
                  + "\n\n";
         }
         // else {
         // string += m.getName() + " (not Computable):\n";
         // }
      }

      return string;
   } // toString

} // ArrayReal
