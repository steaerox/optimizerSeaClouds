//  Permutation.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jmetal.core.Variable;
import jmetal.encodings.variable.Permutation;
import jmetal.util.PseudoRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.platform.planner.optimizer.util.YAMLmodulesOptimizerParserNew;
import eu.seaclouds.platform.planner.optimizer.util.YAMLoptimizerParserNew;

/**
 * Class implementing a permutation of integer decision encodings.variable
 */
@SuppressWarnings("serial")
public class Module implements Cloneable {

   private static final boolean IS_DEBUG = false;

   public static final int DEFAULT_MAX_NUM_OF_INSTANCES = 10;

   static Logger log = LoggerFactory.getLogger(Module.class);

   private String name;

   private CloudOffer bestCloudOffer;

   private int bestCloudOfferIndex;

   private int numOfInstancesForBestCloudOffer;

   private boolean isComputable;

   /**
    * Stores a List of <code>CloudOffer</code> values
    */
   private List<CloudOffer> suitableCloudOfferList;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public CloudOffer getBestCloudOffer() {
      return bestCloudOffer;
   }

   public void setBestCloudOffer(CloudOffer bestCloudOffer) {
      this.bestCloudOffer = bestCloudOffer;
      this.bestCloudOfferIndex = getCloudOfferIndex(bestCloudOffer);
   }

   public CloudOffer getBestCloudOfferFromIndex(int cloudOfferIndex) {
      return suitableCloudOfferList.get(cloudOfferIndex);
   }

   private int getCloudOfferIndex(CloudOffer bestCloudOffer) {

      int index = 0;

      for (CloudOffer tempCloudOffer : suitableCloudOfferList) {
         if ((tempCloudOffer.getName()).equals(bestCloudOffer.getName())) {
            // Names are equal (I could say are really equal, otherwise i
            // have to compare every single field
            return index;
         }
         index++;
      }

      log.error("Some error has occured searching the bestCloudOffer's index");
      return 0;
   }

   public int getBestCloudOfferIndex() {
      return bestCloudOfferIndex;
   }

   private void setBestCloudOfferIndex(int bestCloudOfferIndex) {
      this.bestCloudOfferIndex = bestCloudOfferIndex;
   }

   public void setBestCloudOfferByIndex(int index) {
      this.bestCloudOffer = suitableCloudOfferList.get(index);
      setBestCloudOfferIndex(index);
   }

   public List<CloudOffer> getSuitableCloudOfferList() {
      return suitableCloudOfferList;
   }

   public void setSuitableCloudOfferList_(List<CloudOffer> cloudOfferList) {
      this.suitableCloudOfferList = cloudOfferList;
   }

   public int getNumOfInstancesForBestCloudOffer() {
      return numOfInstancesForBestCloudOffer;
   }

   public void setNumOfInstancesForBestCloudOffer(int numOfInstances) {
      this.numOfInstancesForBestCloudOffer = numOfInstances;
   }

   public boolean isComputable() {
      return isComputable;
   }

   public void setComputable(boolean isComputable) {
      this.isComputable = isComputable;
   }

   public void setBestPACandNumOfInstances(boolean needToChangeInstances) {
      List<CloudOffer> tempCloudList1 = new ArrayList<CloudOffer>();
      List<CloudOffer> tempCloudList2 = new ArrayList<CloudOffer>();
      CloudOffer currentCloudOffer = getBestCloudOffer();

      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getPerformance() > currentCloudOffer.getPerformance()) {
            currentCloudOffer = cloudOffer.clone();
         } else {
            if (cloudOffer.getPerformance() == currentCloudOffer
                  .getPerformance()) {
               tempCloudList2.add(cloudOffer.clone());
            }
         }
      }
      if (!tempCloudList2.isEmpty()) {
         for (CloudOffer cloudOffer : tempCloudList2) {
            if (cloudOffer.getAvailability() > currentCloudOffer
                  .getAvailability()) {
               currentCloudOffer = cloudOffer.clone();
            } else {
               if (cloudOffer.getAvailability() == currentCloudOffer
                     .getAvailability()) {
                  tempCloudList1.add(cloudOffer.clone());
               }
            }
         }
         tempCloudList2.clear();
         if (!tempCloudList1.isEmpty()) {
            for (CloudOffer cloudOffer : tempCloudList1) {
               if (cloudOffer.getCost() < currentCloudOffer.getCost()) {
                  currentCloudOffer = cloudOffer.clone();
               } else {
                  if (cloudOffer.getPerformance() == currentCloudOffer
                        .getPerformance()) {
                     tempCloudList2.add(cloudOffer.clone());
                  }
               }
            }
         }
      }
      if (!tempCloudList2.isEmpty()) {
         setRandomOfferBetween(tempCloudList2);
      } else {
         setBestCloudOffer(currentCloudOffer);
         if (needToChangeInstances) {
            int numOfInstances = getRandomNumOfInstancesDifferentFromCurrent();
            setNumOfInstancesForBestCloudOffer(numOfInstances);
         }
      }
      tempCloudList2.clear();
   }

   private int getRandomNumOfInstancesDifferentFromCurrent() {
      int numOfInstances = PseudoRandom
            .randInt(1, DEFAULT_MAX_NUM_OF_INSTANCES);

      while (numOfInstances == getNumOfInstancesForBestCloudOffer()) {
         numOfInstances = PseudoRandom.randInt(1, DEFAULT_MAX_NUM_OF_INSTANCES);
      }
      return numOfInstances;
   }

   private void setRandomOfferBetween(List<CloudOffer> cloudOfferList) {
      int offerChosen = PseudoRandom.randInt(0, cloudOfferList.size() - 1);
      int itemToUse = getCloudOfferIndex(cloudOfferList.get(offerChosen));

      setBestCloudOfferByIndex(itemToUse);

      int numOfInstances = getRandomNumOfInstancesDifferentFromCurrent();
      setNumOfInstancesForBestCloudOffer(numOfInstances);
   }

   public void setBestCloudOfferBetweenPACequallyRandom() {

      int switcher = PseudoRandom.randInt(1, 3);

      switch (switcher) {
      case 1:
         improveInPerformance();
         break;
      case 2:
         improveInAvailability();
         break;
      case 3:
         improveInCost();
         break;
      default:
         break;
      }

   }

   public void setRandomOffer(boolean needToChangeInstances) {
      int itemToUse = PseudoRandom
            .randInt(0, suitableCloudOfferList.size() - 1);
      setBestCloudOfferByIndex(itemToUse);
      if (needToChangeInstances) {
         int numOfInstances = getRandomNumOfInstancesDifferentFromCurrent();
         setNumOfInstancesForBestCloudOffer(numOfInstances);
      }
   }

   public void setNewInstancesForCurrentBestCloudOffer() {
      int numOfInstances = getRandomNumOfInstancesDifferentFromCurrent();
      setNumOfInstancesForBestCloudOffer(numOfInstances);
   }

   private void improveInPerformance() {
      List<CloudOffer> tempCloudList = new ArrayList<CloudOffer>();
      CloudOffer currentCloudOffer = getBestCloudOffer();

      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getPerformance() > currentCloudOffer.getPerformance()) {
            currentCloudOffer = cloudOffer.clone();
            // changed = true;
         } else {
            if (cloudOffer.getPerformance() == bestCloudOffer.getPerformance()) {
               tempCloudList.add(cloudOffer);
            }
         }
      }

      if (!tempCloudList.isEmpty()) {
         setRandomOfferBetween(tempCloudList);
      }
   }

   private void improveInAvailability() {
      List<CloudOffer> tempCloudList = new ArrayList<CloudOffer>();
      CloudOffer currentCloudOffer = getBestCloudOffer();

      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getAvailability() > bestCloudOffer.getAvailability()) {
            bestCloudOffer = cloudOffer.clone();
         } else {
            if (cloudOffer.getAvailability() == bestCloudOffer
                  .getAvailability()) {
               tempCloudList.add(cloudOffer);
            }
         }
      }
      
      if (!tempCloudList.isEmpty()) {
         setRandomOfferBetween(tempCloudList);
      }
   }

   private void improveInCost() {
      List<CloudOffer> tempCloudList = new ArrayList<CloudOffer>();
      CloudOffer currentCloudOffer = getBestCloudOffer();

      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getCost() < bestCloudOffer.getCost()) {
            bestCloudOffer = cloudOffer.clone();
         } else {
            if (cloudOffer.getCost() == bestCloudOffer.getCost()) {
               tempCloudList.add(cloudOffer);
            }
         }
      }
      
      if (!tempCloudList.isEmpty()) {
         setRandomOfferBetween(tempCloudList);
      }
   }

   public void setBestPerformanceCloudOffer() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getPerformance() > bestCloudOffer.getPerformance()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
   }

   public void setBestPerformanceCloudOfferAndNewInstances() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getPerformance() > bestCloudOffer.getPerformance()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
      setNewInstancesForCurrentBestCloudOffer();
   }

   public void setBestAvailabilityCloudOffer() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getAvailability() < bestCloudOffer.getAvailability()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
   }

   public void setBestAvailabilityCloudOfferAndNewInstances() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getAvailability() < bestCloudOffer.getAvailability()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
      setNewInstancesForCurrentBestCloudOffer();
   }

   public void setBestCostCloudOffer() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getCost() < bestCloudOffer.getCost()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
   }

   public void setBestCostCloudOfferAndNewInstances() {
      for (CloudOffer cloudOffer : suitableCloudOfferList) {
         if (cloudOffer.getCost() < bestCloudOffer.getCost()) {
            bestCloudOffer = cloudOffer.clone();
         }
      }
      setNewInstancesForCurrentBestCloudOffer();
   }

   // Used for Test
   public Module(String moduleName, List<CloudOffer> cloudOfferList) {

      this.name = moduleName;

      setComputable(true);

      this.suitableCloudOfferList = cloudOfferList;

      int itemToUse = PseudoRandom.randInt(0, cloudOfferList.size() - 1);
      // int numInstances = PseudoRandom.randInt(1,
      // DEFAULT_MAX_NUM_OF_INSTANCES);
      //
      setBestCloudOfferByIndex(itemToUse);
      // setNumOfInstancesForBestCloudOffer(numInstances);
      setNumOfInstancesForBestCloudOffer(1);
   }

   public Module(Entry<String, Object> entry,
         Map<String, Object> allCloudOffersMAP, Map<String, Object> moduleMap) {

      name = entry.getKey();
      Map<String, Object> module = (Map<String, Object>) entry.getValue();

      List<String> potentialListOfOffersNames = YAMLoptimizerParserNew
            .GetListOfSuitableOptionsForAlreadyFoundModule(entry.getValue());

      if (potentialListOfOffersNames != null) {
         
         //setType(YAMLmodulesOptimizerParserNew.getHostOfModule(module));
         
         if (IS_DEBUG) {
            log.debug("Found suitable options, saving their reference. Module name= "
                  + name
                  + " cloud offers="
                  + potentialListOfOffersNames.toString());
         }
         suitableCloudOfferList = YAMLoptimizerParserNew
               .getCloudOfferCharacteristcisByName(potentialListOfOffersNames,
                     allCloudOffersMAP);

         setComputable(true);

         int itemToUse = findSuitableOfferToUse();
         int numInstances = PseudoRandom.randInt(1,
               DEFAULT_MAX_NUM_OF_INSTANCES);

         setBestCloudOfferByIndex(itemToUse);
         setNumOfInstancesForBestCloudOffer(numInstances);

      } else {// Suitable Options not found (Example: NuroCaseStudy)
         if (IS_DEBUG) {
            log.debug("Suitable Options not found (For example in NuroCaseStudy)");
            setComputable(false);
         }
      }
   }
   
   private int findSuitableOfferToUse(){
      int itemToUse = PseudoRandom.randInt(0,
            suitableCloudOfferList.size() - 1);
      
      //Theoretically useless because suitableCloudOffer are all for the same type (knowing that Matchmaker
      //has done correctly the list.      
//      try {
//         while(!suitableCloudOfferList.get(itemToUse).getType().equals(getType())){
//            itemToUse = PseudoRandom.randInt(0,
//                  suitableCloudOfferList.size() - 1);
//         }
//      } catch (Exception e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }

//      while(suitableCloudOfferList.get(itemToUse).getName().equals(bestCloudOffer.getName())){
//         itemToUse = PseudoRandom.randInt(0,
//               suitableCloudOfferList.size() - 1);
//      }
      
      return itemToUse;
      
   }

   public static List<CloudOffer> cloneList(List<CloudOffer> list) {
      List<CloudOffer> clone = new ArrayList<CloudOffer>(list.size());
      for (CloudOffer item : list)
         clone.add(item.clone());
      return clone;
   }

   /**
    * Copy Constructor
    * 
    * @param module
    *           The Module to copy
    */
   public Module(Module module) {

      this.name = module.getName();
      this.isComputable = module.isComputable();
      if (module.isComputable) {
         this.suitableCloudOfferList = cloneList(module.suitableCloudOfferList);
         this.bestCloudOffer = module.bestCloudOffer.clone();
         this.bestCloudOfferIndex = module.getBestCloudOfferIndex();
         this.numOfInstancesForBestCloudOffer = module
               .getNumOfInstancesForBestCloudOffer();
      }
//      this.tempList = cloneList(tempList);

   } // Module

   @Override
   protected Module clone() throws CloneNotSupportedException {
      // return (Module) super.clone();
      return new Module(this);
   }

   /**
    * Returns a string representing the object
    * 
    * @return The string
    */
   @Override
   public String toString() {
      String string;

      string = "";

      if (isComputable()) {
         string += getName() + ":\n" + "\tBestCloudOffer: "
               + getNumOfInstancesForBestCloudOffer() + " instances of "
               + getBestCloudOffer().getName() + "\n" + "\t\tPerformance: "
               + getBestCloudOffer().getPerformance() + "\n\t\tAvailability: "
               + getBestCloudOffer().getAvailability() + "\n\t\tCost [€/h]: "
               + getBestCloudOffer().getCost() + "\n\n";
      } else {
         string += getName() + " (not Computable):\n\n";
      }

      return string;
   } // toString

} // Permutation
