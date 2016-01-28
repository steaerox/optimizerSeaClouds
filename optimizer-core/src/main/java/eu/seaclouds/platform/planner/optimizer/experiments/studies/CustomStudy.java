//ManyAlgStudy.java

package eu.seaclouds.platform.planner.optimizer.experiments.studies;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jmetal.core.Algorithm;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.experiments.util.Friedman;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.experiments.settings.MOCell_SeaClouds_Settings;
import eu.seaclouds.platform.planner.optimizer.experiments.settings.NSGAII_SeaClouds_Settings;
import eu.seaclouds.platform.planner.optimizer.experiments.settings.PAES_SeaClouds_Settings;
import eu.seaclouds.platform.planner.optimizer.experiments.settings.RandomSearch_SeaClouds_Settings;
import eu.seaclouds.platform.planner.optimizer.experiments.settings.SPEA2_SeaClouds_Settings;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristicsName;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.MOCellSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.NSGAIISearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.PAESSearch;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.Random;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.SPEA2Search;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.multiobjective.NSGAIISearch.Builder;
import eu.seaclouds.platform.planner.optimizer.operator.crossover.CrossoverOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.MutationOperatorName;
import eu.seaclouds.platform.planner.optimizer.operator.selection.SelectionOperatorName;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblem;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;
import eu.seaclouds.platform.planner.optimizer.util.jMetalConfDir;

public class CustomStudy extends Experiment {

   static Logger log = LoggerFactory.getLogger(CustomStudy.class);

   private Map<String, Object> appMap;
   private Map<String, Object> suitableCloudOffersMap;
   private Topology topology;

   private int numberOfThreads;

   private String projectOutputDirectory = "src/test/target/";

   private ArrayList<MetaHeuristic> metaHeuristicList = new ArrayList<MetaHeuristic>();

   private boolean notch;

   public static class Builder {

      private Map<String, Object> appMap;
      private Map<String, Object> suitableCloudOffersMap;
      private Topology topology;
      private int numberOfThreads = 1;

      private String experimentName_ = "General";

      private ArrayList<String> algorithmNameArrayList = new ArrayList<String>(
            Arrays.asList("Random", "NSGAII", "MOCell", "PAES", "SPEA2"));

      // List of problems to be solved
      private ArrayList<String> problemArrayList_ = new ArrayList<String>(
            Arrays.asList("SeaCloudsProblem"));
//      private ArrayList<String> problemArrayList_ = new ArrayList<String>(
//            Arrays.asList("SeaCloudsProblem1","SeaCloudsProblem2","SeaCloudsProblem3","SeaCloudsProblem4"));      
      
      // The starting ParetoFront is unknown for the Problem
//      private String[] paretoFrontFile_ = new String[] { "" };
      private String[] paretoFrontFile_ = new String[] { "", "", "", "" };

      // List of the quality indicators to be applied
      // private String[] indicatorList_ = new String[] { "HV", "IGD", "EPSILON"
      // };
      private ArrayList<String> indicatorArrayList_ = new ArrayList<String>(
            Arrays.asList("HV", "IGD", "EPSILON"));

      // Directory containing the Pareto front files
      private String paretoFrontDirectory_ = "";

      // Number of independent runs per algorithm
      private int independentRuns_ = 2;

      private ArrayList<MetaHeuristic> metaHeuristicList = new ArrayList<MetaHeuristic>();
      private String[] algorithmNameList_;
      private boolean isIndicatorListPreSet;
      private boolean notch = false;
      private boolean isUniqueAlgorithmsList = true;
      private String[] problemList_;
      private String[] indicatorList_;

      public Builder() throws JMException {
         // Loading Default MetaHeurstic Settings
         for (String algorithmName : algorithmNameArrayList) {
            switch (algorithmName) {
            case "Random":
               metaHeuristicList.add(new Random.Builder(appMap,
                     suitableCloudOffersMap, topology).build());
               break;
            case "NSGAII":
               metaHeuristicList.add(new NSGAIISearch.Builder(appMap,
                     suitableCloudOffersMap, topology).setPopulationSize(4)
                     .build());
               break;
            case "MOCell":
               metaHeuristicList.add(new MOCellSearch.Builder(appMap,
                     suitableCloudOffersMap, topology).setPopulationSize(4)
                     .build());
               break;
            case "PAES":
               metaHeuristicList.add(new PAESSearch.Builder(appMap,
                     suitableCloudOffersMap, topology).build());
               break;
            case "SPEA2":
               metaHeuristicList.add(new SPEA2Search.Builder(appMap,
                     suitableCloudOffersMap, topology).setPopulationSize(4)
                     .build());
               break;
            default:
               metaHeuristicList.add(new Random.Builder(appMap,
                     suitableCloudOffersMap, topology).build());
               // TODO:Complete with more methods
            }
         }
         
         this.isUniqueAlgorithmsList  = false;
      }

      public Builder(ArrayList<MetaHeuristic> metaHeuristicList) {
         this.algorithmNameArrayList.clear();
         this.metaHeuristicList = metaHeuristicList;
         if (!metaHeuristicList.isEmpty() || metaHeuristicList != null) {
            for (int i = 0; i < metaHeuristicList.size(); i++) {
               String algorithmName = metaHeuristicList.get(i).getAlgorithm()
                     .getClass().getSimpleName();
               search(algorithmName, i, 1);
               this.algorithmNameArrayList.add(metaHeuristicList.get(i)
                     .getMetaHeuristicName());
            }
         }
         String[] tempArr = new String[this.algorithmNameArrayList.size()];
         this.algorithmNameList_ = this.algorithmNameArrayList
               .toArray(tempArr);

      }

      public Builder setNumberOfThreads(int numberOfThreads) {
         this.numberOfThreads = numberOfThreads;
         return this;
      }

      public Builder setExperimentName(String experimentName) {
         this.experimentName_ = experimentName;
         return this;
      }

      public Builder setIndependentRuns(int independentRuns) {
         this.independentRuns_ = independentRuns;
         return this;
      }

      public Builder setTrueNotchForBoxplot() {
         this.notch = true;
         return this;
      }

      public Builder setIndicator(QualityIndicatorName qualityIndicator) {
         if (this.isIndicatorListPreSet == true) {
            this.indicatorArrayList_.clear();
            this.isIndicatorListPreSet = false;
         } else {
            this.indicatorArrayList_.add(qualityIndicator.getIndicatorName());
         }
         return this;
      }

      private String getCharForNumber(int i) {
         return i > 0 && i < 27 ? String.valueOf((char) (i + 96)) : null;
      }

      private void search(String algorithmName, int index,
            int sameAlgorithmIndex) {
         for (int i = index + 1; i < this.metaHeuristicList.size(); i++) {
            if (metaHeuristicList.get(i).getMetaHeuristicName()
                  .contains(algorithmName)) {
               if (metaHeuristicList.get(i).getMetaHeuristicName()
                     .equals(algorithmName)) {
                  if (sameAlgorithmIndex == 1) {
                     metaHeuristicList.get(index).setMetaHeuristicName(
                           algorithmName + getCharForNumber(sameAlgorithmIndex));
                  }
                  metaHeuristicList.get(i).setMetaHeuristicName(
                        algorithmName + getCharForNumber(++sameAlgorithmIndex));
   
               }
            }
            else{
               this.isUniqueAlgorithmsList = false;
            }
         }
      }

      public CustomStudy build() throws JMException, IOException {
         
         String[] tempArr = new String[problemArrayList_.size()];
         this.problemList_ = problemArrayList_.toArray(tempArr);
         
         tempArr = new String[indicatorArrayList_.size()];
         this.indicatorList_ = indicatorArrayList_.toArray(tempArr);
         
         if(isUniqueAlgorithmsList){
            this.experimentName_ = this.metaHeuristicList.get(0).getAlgorithm().getClass().getSimpleName();
         }
         
         return new CustomStudy(this).startExperiment();
      }
   }

   public CustomStudy(Builder builder) throws JMException, IOException {

      this.appMap = builder.metaHeuristicList.get(0).getAppMap();
      this.suitableCloudOffersMap = builder.metaHeuristicList.get(0)
            .getSuitableCloudOfferMap();
      this.topology = builder.metaHeuristicList.get(0).getTopology();

      this.metaHeuristicList = builder.metaHeuristicList;

      this.numberOfThreads = builder.numberOfThreads;
      this.experimentName_ = builder.experimentName_ + "Study";

      this.algorithmNameList_ = builder.algorithmNameList_;
      
      this.problemList_ = builder.problemList_;

      this.paretoFrontFile_ = builder.paretoFrontFile_;
      
      this.indicatorList_ = builder.indicatorList_;

      this.paretoFrontDirectory_ = builder.paretoFrontDirectory_;

      this.notch = builder.notch;

      this.independentRuns_ = builder.independentRuns_;
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm");
      // Get an instance of a Calendar, using the current time.
      Calendar calendar = Calendar.getInstance();
      String currentDate = dateFormat.format(calendar.getTime());

      int numberOfAlgorithms = this.metaHeuristicList.size();

      // Paremeter experiments.settings of each algorithm
      this.algorithmSettings_ = new Settings[numberOfAlgorithms];
      this.experimentBaseDirectory_ = projectOutputDirectory
            + builder.experimentName_ + "_" + this.independentRuns_ + "runs_"
            + currentDate + "/" + this.experimentName_;

   }

   private CustomStudy startExperiment() throws JMException, IOException {
      initExperiment();
      // Run the experiments
      // TODO: Fix SeaCloudsProblem working with Threads
      // int numberOfThreads = Runtime.getRuntime().availableProcessors();
      
      
      
      runExperiment(this.numberOfThreads);
   
      
      generateQualityIndicators();

      // Generate latex tables
      generateLatexTables() ;
      
      // Configure the R scripts to be generated
      int rows = 1;
      int columns = 1;
      String prefix = new String(this.experimentName_);

      // Configuring scripts for SeaCloudsProblem
      generateRBoxplotScripts(rows, columns, this.problemList_, prefix,
            this.notch, this);
      generateRWilcoxonScripts(this.problemList_, prefix, this);

      // Applying Friedman test
      Friedman test = new Friedman(this);
      for (int i = 0; i < indicatorList_.length; i++) {
         test.executeTest(indicatorList_[i]); // "HV", "IGD", "EPSILON"
      }
      return this;
   }

   /**
    * Configures the algorithms in each independent run
    * 
    * @param problemName
    *           The problem to solve
    * @param problemIndex
    * @throws ClassNotFoundException
    */
   @Override
   public void algorithmSettings(String problemName, int problemIndex,
         Algorithm[] algorithm) throws ClassNotFoundException {
      try {
         int numberOfAlgorithms = algorithmNameList_.length;

         HashMap[] parameters = new HashMap[numberOfAlgorithms];

         for (int i = 0; i < numberOfAlgorithms; i++) {
            parameters[i] = new HashMap();
         } // for

         if (!paretoFrontFile_[problemIndex].equals("")) {
            for (int i = 0; i < numberOfAlgorithms; i++)
               parameters[i].put("paretoFrontFile_",
                     paretoFrontFile_[problemIndex]);
         } // if
         
         Object[] problemParams = new Object[3];
         problemParams[0] = this.appMap;
         problemParams[1] = this.suitableCloudOffersMap;
         problemParams[2] = this.topology;

         for (int i = 0; i < numberOfAlgorithms; i++) {
            parameters[i].put("crossoverProbability_",
                  (double) metaHeuristicList.get(i).getCrossoverProbability());
            parameters[i].put("mutationProbability", (double) metaHeuristicList
                  .get(i).getMutationProbability());
         }

         if ((!paretoFrontFile_[problemIndex].equals(""))
               || (paretoFrontFile_[problemIndex] == null)) {
            for (int i = 0; i < numberOfAlgorithms; i++)
               parameters[i].put("paretoFrontFile_",
                     paretoFrontFile_[problemIndex]);
         } // if

         int algIndex = 0;
         for (MetaHeuristic metaHeuristic : this.metaHeuristicList) {

            String algorithmName = metaHeuristic.getAlgorithm().getClass()
                  .getSimpleName();

            switch (algorithmName) {
            case "RandomSearch": {
               algorithm[algIndex] = new RandomSearch_SeaClouds_Settings(
                     problemName, problemParams, metaHeuristic).configure();
               break;
            }
            case "NSGAII": {
               algorithm[algIndex] = new NSGAII_SeaClouds_Settings(problemName,
                     problemParams, metaHeuristic).configure();
               break;
            }
            // case "IBEA": {
            // algorithm[i] = new IBEA_SeaClouds_Settings(problemName,
            // problemParams).configure(parameters[i]);
            // break;
            // }
            case "MOCell": {
               algorithm[algIndex] = new MOCell_SeaClouds_Settings(problemName,
                     problemParams, metaHeuristic).configure();
               break;
            }
            case "PAES": {
               algorithm[algIndex] = new PAES_SeaClouds_Settings(problemName,
                     problemParams, metaHeuristic).configure();
               break;
            }
            case "SPEA2": {
               algorithm[algIndex] = new SPEA2_SeaClouds_Settings(problemName,
                     problemParams, metaHeuristic).configure();
               break;
            }
            // case "SMSEMOA": {
            // algorithm[i] = new SMSEMOA_SeaClouds_Settings(problemName,
            // problemParams).configure(parameters[i]);
            // break;
            // }
            default:
               log.error("NO Algorithm Found");
               break;
            }
            algIndex++;
         }

      } catch (IllegalArgumentException ex) {
         // Logger.getLogger(ManyAlgStudy.class.getName()).log(Level.SEVERE,
         // null, ex);
         log.error(ex.getMessage());
      } catch (JMException ex) {
         // Logger.getLogger(ManyAlgStudy.class.getName()).log(Level.SEVERE,
         // null, ex);
         log.error(ex.getMessage());
      }
   } // algorithmSettings

   public void initConf(String algName) throws FileNotFoundException,
         IOException {
      // configuration_ = new Properties();
      // InputStreamReader isr = new InputStreamReader(new
      // FileInputStream(jMetalHome.jMetalHomeConfDir+"/NSGAII.conf"));
      // configuration_.load(isr);

      Properties configuration_ = new Properties();
      InputStreamReader isr = new InputStreamReader(new FileInputStream(
            jMetalConfDir.jMetalHomeConfDir + "/" + algName + ".conf"));
      configuration_.load(isr);

   }

}