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

package eu.seaclouds.platform.planner.optimizerTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jmetal.core.Solution;
import jmetal.experiments.Settings;
import jmetal.experiments.util.Friedman;
import jmetal.util.JMException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.seaclouds.platform.planner.optimizer.Optimizer;
import eu.seaclouds.platform.planner.optimizer.OptimizerNew;
import eu.seaclouds.platform.planner.optimizer.experiments.studies.ExperimentStudy;
import eu.seaclouds.platform.planner.optimizer.experiments.studies.StudiesName;
import eu.seaclouds.platform.planner.optimizer.heuristics.SearchMethodName;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristicsName;
import eu.seaclouds.platform.planner.optimizer.util.TOSCAkeywords;

public class OptimizerTest {

   private static Optimizer optimizer;
   private static String appModel;
   private static String suitableCloudOffers;

   private static final String APP_MODEL_FILENAME_JMETAL_VERSION = "./src/test/resources/MatchmakeroutputWithQoSAutomatic.yaml";
   private static final String CLOUD_OFFER_FILENAME_JMETAL_VERSION = "./src/test/resources/cloudOfferWithQoS.yaml";

   static Logger log;
   private OptimizerNew optimizerNew;
   private String currentDate;
   private ExperimentStudy experimentStudy;

   @BeforeClass
   public void createObjects() {

      log = LoggerFactory.getLogger(OptimizerTest.class);
      log.info("Starting TEST optimizer");

      final String dir = System.getProperty("user.dir");
      log.debug("Trying to open files: current executino dir = " + dir);

      try {
         appModel = filenameToString(TestConstants.APP_MODEL_FILENAME);
      } catch (IOException e) {
         log.error("File for APPmodel not found");
         e.printStackTrace();
      }

      try {
         suitableCloudOffers = filenameToString(TestConstants.CLOUD_OFFER_FILENAME);
      } catch (IOException e) {
         log.error("File for Cloud Offers not found");
         e.printStackTrace();
      }

   }

   private static String filenameToString(String path) throws IOException {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, StandardCharsets.UTF_8);
   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testPresenceSolutionBlind() {

      log.info("=== TEST for SOLUTION GENERATION of BLIND optimizer STARTED ===");

      optimizer = new Optimizer(TestConstants.NUM_PLANS_TO_GENERATE,
            SearchMethodName.BLINDSEARCH);

      String[] arrayDam = optimizer.optimize(appModel, suitableCloudOffers);
      for (int damnum = 0; damnum < arrayDam.length; damnum++) {

         try {
            checkCorrectness(arrayDam[damnum]);
         } catch (Exception e) {
            log.error("There was an error in the check of correctness. Solution was: "
                  + arrayDam[damnum]);
            throw e;
         }
         saveFile(TestConstants.OUTPUT_FILENAME + SearchMethodName.BLINDSEARCH
               + damnum + ".yaml", arrayDam[damnum]);
      }

      log.info("=== TEST for SOLUTION GENERATION of BLIND optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testPresenceSolutionHillClimb() {

      log.info("=== TEST for SOLUTION GENERATION of HILLCLIMB optimizer STARTED ===");

      optimizer = new Optimizer(TestConstants.NUM_PLANS_TO_GENERATE,
            SearchMethodName.HILLCLIMB);

      String[] arrayDam = optimizer.optimize(appModel, suitableCloudOffers);
      for (int damnum = 0; damnum < arrayDam.length; damnum++) {

         try {
            checkCorrectness(arrayDam[damnum]);
         } catch (Exception e) {
            log.error("There was an error in the check of correctness. Solution was: "
                  + arrayDam[damnum]);
            throw e;
         }
         saveFile(TestConstants.OUTPUT_FILENAME + SearchMethodName.HILLCLIMB
               + damnum + ".yaml", arrayDam[damnum]);

      }

      log.info("=== TEST for SOLUTION GENERATION of HILLCLIMB optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testPresenceSolutionAnneal() {

      log.info("=== TEST for SOLUTION GENERATION of ANNEAL optimizer STARTED ===");

      optimizer = new Optimizer(TestConstants.NUM_PLANS_TO_GENERATE,
            SearchMethodName.ANNEAL);

      String[] arrayDam = optimizer.optimize(appModel, suitableCloudOffers);
      for (int damnum = 0; damnum < arrayDam.length; damnum++) {

         try {
            checkCorrectness(arrayDam[damnum]);
         } catch (Exception e) {
            log.error("There was an error in the check of correctness. Solution was: "
                  + arrayDam[damnum]);
            throw e;
         }
         saveFile(TestConstants.OUTPUT_FILENAME + SearchMethodName.ANNEAL
               + damnum + ".yaml", arrayDam[damnum]);

      }

      log.info("=== TEST for SOLUTION GENERATION of ANNEAL optimizer FINISEHD ===");

   }

   private void checkCorrectness(String dam) {

      Assert.assertFalse("Dam was not created, optimize method returns null",
            dam == null);
      String damLines[] = dam.split(System.getProperty("line.separator"));

      Assert.assertTrue("Dam was not created", damLines.length > 1);

      int numServices = 0;
      int numSuitableServicesFound = 0;

      for (String line : damLines) {
         if ((line != null) && (line.contains(TOSCAkeywords.SUITABLE_SERVICES))) {

            numServices++;
            String suitableServicesLine[] = line
                  .split(TestConstants.OPEN_SQUARE_BRACKET);

            for (String suitableLine : suitableServicesLine) {
               if ((suitableLine != null)
                     && suitableLine
                           .contains(TestConstants.CLOSE_SQUARE_BRACKET)) {
                  String suitableService = suitableLine.substring(0,
                        suitableLine
                              .indexOf(TestConstants.CLOSE_SQUARE_BRACKET));
                  Assert.assertTrue("Suitable service is the empty string",
                        suitableService != "");
                  Assert.assertTrue(
                        "Suitable service chosen does not belong to the cloud offer",
                        suitableCloudOffers.contains(suitableService));
                  numSuitableServicesFound++;
               }
            }

         }
      }
      Assert.assertEquals("Optimizer did not find any of the services",
            numServices, numSuitableServicesFound);

   }

   private void saveFile(String outputFilename, String dam) {
      PrintWriter out = null;
      try {
         File file = new File(outputFilename);
         if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
         }
         out = new PrintWriter(new FileWriter(file));
         out.println(dam);
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (out != null) {
            out.close();
         }
      }

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testPerformanceComplete() {

      optimizer = new Optimizer();

      log.info("=== TEST for PERFORMANCE of optimizer STARTED ===");
      long startTime = System.currentTimeMillis();
      optimizer.optimize(appModel, suitableCloudOffers);
      long finishTime = System.currentTimeMillis();

      log.debug("Optimizer execution time= "
            + ((finishTime - startTime) / 1000.0) + " seconds");
      Assert.assertTrue("Otimizer does not have good Performance. More than "
            + (TestConstants.MAX_MILLIS_EXECUTING) / 1000.1 + " seconds",
            (finishTime - startTime) < TestConstants.MAX_MILLIS_EXECUTING);
      log.info("=== TEST for PERFORMANCE of optimizer FINISHED===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testRandom() {

      log.info("=== TEST for SOLUTION GENERATION of RANDOM optimizer STARTED ===");

      readProblemFiles();
      
      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.RANDOM);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      
      
      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of RANDOM optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testNSGAII() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of NSGAII optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.NSGAII);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      double globalFitness = 0;
      
//      for(int i=0; i<bestSolution.getNumberOfObjectives(); i++){
//         switch(i){
//         case 0:
//            bestSol+= "Performance: " + bestSolution.getObjective(i) + "\n";
//            break;
//         case 1:
//            bestSol+= "Availability: " + bestSolution.getObjective(i) + "\n";
//            break;
//         case 2:
//            bestSol+= "Cost: " + bestSolution.getObjective(i) + "\n";
//            break;
//         }
//         globalFitness+=bestSolution.getObjective(i);
//      }
//      
//      bestSol+="GlobalFitness: " + globalFitness + "\n";
      
      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of NSGAII optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testMOCell() {

      log.info("=== TEST for SOLUTION GENERATION of MOCell optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.MOCell);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of MOCell optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testPAES() {

      log.info("=== TEST for SOLUTION GENERATION of PAES optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.PAES);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of PAES optimizer FINISEHD ===");

   }

   @Test(enabled = TestConstants.EnabledTest)
   public void testSPEA2() {

      log.info("=== TEST for SOLUTION GENERATION of SPEA2 optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.SPEA2);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of SPEA2 optimizer FINISEHD ===");

   }
   
   
   //Test for Single-Objective Problem
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testAsyncCellularGA() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of AsyncCellularGA optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.AsyncCellularGA);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of AsyncCellularGA optimizer FINISEHD ===");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testElitistES() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of ElitistES optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.ElitistES);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of ElitistES optimizer FINISEHD ===");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testGenerationalGA() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of GenerationalGA optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.GenerationalGA);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of GenerationalGA optimizer FINISEHD ===");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testNonElitistES() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of NonElitistES optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.NonElitistES);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of NonElitistES optimizer FINISEHD ===");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testSteadyStateGA() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of SteadyStateGA optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.SteadyStateGA);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of SteadyStateGA optimizer FINISEHD ===");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testSyncCellularGA() throws IOException {

      log.info("=== TEST for SOLUTION GENERATION of SyncCellularGA optimizer STARTED ===");

      readProblemFiles();

      try {
         optimizerNew = new OptimizerNew(appModel, suitableCloudOffers,
               MetaHeuristicsName.SyncCellularGA);
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Solution bestSolution = optimizerNew.optimize();

      String bestSol = "";

      int numOfVariables = bestSolution.getDecisionVariables().length;
      for (int i = 0; i < numOfVariables; i++) {
         bestSol += bestSolution.getDecisionVariables()[i].toString();
      }

      log.info("\n\nBest Solution found:\n\n" + bestSol);

      log.info("=== TEST for SOLUTION GENERATION of SyncCellularGA optimizer FINISEHD ===");

   }
   

   private void readProblemFiles() {

      log.info("Starting TEST optimizer");

      // Get an instance of a Calendar, using the current time.
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
      Calendar calendar = Calendar.getInstance();
      currentDate = dateFormat.format(calendar.getTime());

      final String dir = System.getProperty("user.dir");
      log.debug("Trying to open files: current executino dir = " + dir);

      try {
         appModel = filenameToString(APP_MODEL_FILENAME_JMETAL_VERSION);
      } catch (IOException e) {
         log.error("File for APPmodel not found");
         e.printStackTrace();
      }

      try {
         suitableCloudOffers = filenameToString(CLOUD_OFFER_FILENAME_JMETAL_VERSION);
      } catch (IOException e) {
         log.error("File for Cloud Offers not found");
         e.printStackTrace();
      }
      //
      // // Parameters.initializeProblem(appModel, suitableCloudOffer);
      // // Get app characteristics
      // appMap = YAMLoptimizerParserNew.GetMAPofAPP(appModel);
      //
      // // Get cloud offers
      // suitableCloudOfferMAP = YAMLoptimizerParserNew
      // .getMAPofCloudOffers(suitableCloudOffer);
      //
      // topology = YAMLoptimizerParserNew.getApplicationTopology(appMap,
      // suitableCloudOfferMAP);

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testAllAlgorithmsExperiment() {

      log.info("=== TEST Experiment for AllAlgorithms STARTED ===");

      readProblemFiles();      
      
      try {
      
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.AllAlgorithms);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for AllAlgorithms FINISEHD ===");

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testRandomExperiment() {

      log.info("=== TEST Experiment for Random STARTED ===");

      readProblemFiles();
      
      try {
         
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.Random);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for Random FINISEHD ===");

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testNSGAIIExperiment() {

      log.info("=== TEST Experiment for NSGAII STARTED ===");

      readProblemFiles();
      
      try {
         
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.NSGAII);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for NSGAII FINISEHD ===");

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testMOCellExperiment() {

      log.info("=== TEST Experiment for MOCell STARTED ===");

      readProblemFiles();
      
      try {
         
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.MOCell);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for MOCell FINISEHD ===");

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testPAESExperiment() {

      log.info("=== TEST Experiment for PAES STARTED ===");

      readProblemFiles();
      
      try {
         
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.PAES);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for PAES FINISEHD ===");

   }
   
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testSPEA2Experiment() {

      log.info("=== TEST Experiment for SPEA2 STARTED ===");

      readProblemFiles();
      
      try {
         
         experimentStudy = new ExperimentStudy(appModel, suitableCloudOffers, StudiesName.SPEA2);
         experimentStudy.startTest();
         
      } catch (JMException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      log.info("=== TEST Experiment for SPEA2 FINISEHD ===");

   }
   

   @AfterClass
   public void testFinishced() {
      log.info("===== ALL TESTS FOR OPTIMIZER FINISHED ===");
   }
}
