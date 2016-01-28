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

package eu.seaclouds.platform.planner.optimizerTest.nfp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.seaclouds.platform.planner.optimizer.CloudOffer;
import eu.seaclouds.platform.planner.optimizer.Module;
import eu.seaclouds.platform.planner.optimizer.Solution;
import eu.seaclouds.platform.planner.optimizer.SuitableOptions;
import eu.seaclouds.platform.planner.optimizer.Topology;
import eu.seaclouds.platform.planner.optimizer.TopologyElement;
import eu.seaclouds.platform.planner.optimizer.nfp.QualityAnalyzer;
import eu.seaclouds.platform.planner.optimizer.nfp.QualityInformation;
import eu.seaclouds.platform.planner.optimizer.nfp.SeaCloudsQualityAnalyzerNew;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;
import eu.seaclouds.platform.planner.optimizerTest.TestConstants;

public class SeaCloudsQualityAnalyzerTest {

   private static final String CLOUD_OFFER_FILENAME_JMETAL_VERSION = "./src/test/resources/cloudOfferWithQoS.yaml";
   
   private static QualityAnalyzer analyzer;
   static Logger log = LoggerFactory.getLogger(SeaCloudsQualityAnalyzerTest.class);;
   private SeaCloudsQualityAnalyzerNew qualityAnalyzer;
   private Map<String, Object> suitableCloudOffersMap;
   private static String suitableCloudOffer;

   private static String filenameToString(String path) throws IOException {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, StandardCharsets.UTF_8);
   }
   
   @BeforeClass
   public void createObjects() {

      try {
         suitableCloudOffer = filenameToString(CLOUD_OFFER_FILENAME_JMETAL_VERSION);
      // Get cloud offers
         suitableCloudOffersMap = YAMLoptimizerParserNew
               .getMAPofCloudOffers(suitableCloudOffer);
         log.debug("suitableCloudOffersMap READ");
      } catch (IOException e) {
         log.error("File for Cloud Offers not found");
         e.printStackTrace();
      }

      log.info("Starting TEST quality analyzer");
      qualityAnalyzer = new SeaCloudsQualityAnalyzerNew(suitableCloudOffersMap);
   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testJMetalPerformanceEvaluation() {

      log.info("==== TEST for PERFORMANCE EVALUATION starts ====");
      
      List<List<CloudOffer>> listOfCloudOfferListPerModule = createListOfCloudOfferList();
      List<Module> moduleList = createModuleList(listOfCloudOfferListPerModule);
      Topology topology = createTopology();

      double workload = 10;
      
      double cost = qualityAnalyzer.computePerformance(moduleList, topology, workload);

      Assert.assertTrue("Compute performance returned 0", cost != 0);

      log.info("Testing performance. Returned application response time is " + cost);

      log.info("==== TEST for PERFORMANCE EVALUATION finishes ====");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testJMetalAvailabilityEvaluation() {

      log.info("==== TEST for AVAILABILITY EVALUATION starts ====");

      List<List<CloudOffer>> listOfCloudOfferListPerModule = createListOfCloudOfferList();
      List<Module> moduleList = createModuleList(listOfCloudOfferListPerModule);
      Topology topology = createTopology();
      
      double availability = qualityAnalyzer.computeAvailability(moduleList, topology);

      Assert.assertTrue("Compute availability returned an impossible value", availability >= 0.0);
      Assert.assertTrue("Compute availability returned an impossible value", availability <= 1.0);

      log.info("Testing availability. Returned application availability is " + availability);

      log.info("==== TEST for AVAILABILITY EVALUATION finishes ====");

   }
   
   @Test(enabled = TestConstants.EnabledTest)
   public void testJMetalCostEvaluation() {

      log.info("==== TEST for COST EVALUATION starts ====");

      List<List<CloudOffer>> listOfCloudOfferListPerModule = createListOfCloudOfferList();
      List<Module> moduleList = createModuleList(listOfCloudOfferListPerModule);
      
      double cost = qualityAnalyzer.computeCost(moduleList);

      Assert.assertTrue("Compute cost returned an impossible value", cost >= 0.0);

      log.info("Testing cost. Returned application cost is " + cost);

      log.info("==== TEST for COST EVALUATION finishes ====");

   }
   
   
   private static List<List<CloudOffer>> createListOfCloudOfferList(){
      CloudOffer offer1 = new CloudOffer("CloudOffer1", 20, 0.99, 2);
      CloudOffer offer2 = new CloudOffer("CloudOffer2", 30, 0.95, 3);

      List<CloudOffer> cloudOfferListforModule1 = new ArrayList<CloudOffer>();
      cloudOfferListforModule1.add(offer1);
      List<CloudOffer> cloudOfferListforModule2 = new ArrayList<CloudOffer>();
      cloudOfferListforModule2.add(offer2);
      
      List<List<CloudOffer>> listOfCloudOfferListPerModule = new ArrayList<List<CloudOffer>>();
      listOfCloudOfferListPerModule.add(cloudOfferListforModule1);
      listOfCloudOfferListPerModule.add(cloudOfferListforModule2);
      
      return listOfCloudOfferListPerModule;
   }
   
   private static List<Module> createModuleList(List<List<CloudOffer>> listOfCloudOfferListPerModule){
      Module m1 = new Module("Module1", listOfCloudOfferListPerModule.get(0));
      Module m2 = new Module("Module2", listOfCloudOfferListPerModule.get(1));
      
      List<Module> moduleList = new ArrayList<Module>();
      moduleList.add(m1);
      moduleList.add(m2);
      return moduleList;
   }

   private static Topology createTopology() {

      TopologyElement e1 = new TopologyElement("Module1", 1.0); // name and
                                                                // execution
                                                                // time
      TopologyElement e2 = new TopologyElement("Module2", 2.0); // name and
                                                                // execution
                                                                // time

      e1.addElementCalled(e2);

      Topology topology = new Topology();
      topology.addModule(e1);
      topology.addModule(e2);

      return topology;

   }

   @AfterClass
   public void testFinishced() {
      log.info("Test finished");
   }

}
