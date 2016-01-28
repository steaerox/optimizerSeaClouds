//  PAES_Settings.java 
//
//  Authors:
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

package eu.seaclouds.platform.planner.optimizer.experiments.settings;

import java.util.HashMap;
import java.util.Properties;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.paes.PAES;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import eu.seaclouds.platform.planner.optimizer.metaheuristics.MetaHeuristic;
import eu.seaclouds.platform.planner.optimizer.problem.SeaCloudsProblemFactory;

/**
 * Settings class of algorithm PAES
 */
public class PAES_SeaClouds_Settings extends Settings {

   public int maxEvaluations_;
   public int archiveSize_;
   public int biSections_;
   public double mutationProbability_;
   public double mutationDistributionIndex_;

   private MetaHeuristic metaHeuristic;

   /**
    * Constructor
    */
   public PAES_SeaClouds_Settings(String problem, Object[] problemParams,
         MetaHeuristic metaHeuristic) {
      super(problem);

      try {
         problem_ = (new SeaCloudsProblemFactory()).getProblem(problemName_,
               problemParams);
      } catch (JMException e) {
         e.printStackTrace();
      }
      // Default experiments.settings
      this.metaHeuristic = metaHeuristic;

   } // PAES_Settings

   /**
    * Configure the MOCell algorithm with default parameter experiments.settings
    * 
    * @return an algorithm object
    * @throws jmetal.util.JMException
    */
   @Override
   public Algorithm configure() throws JMException {

      return metaHeuristic.getAlgorithm();

   } // configure

   /**
    * Configure PAES with user-defined parameter experiments.settings
    * 
    * @return A PAES algorithm object
    */
   @Override
   public Algorithm configure(Properties configuration) throws JMException {
      Algorithm algorithm;
      Mutation mutation;

      HashMap parameters; // Operator parameters

      // Creating the algorithm.
      algorithm = new PAES(problem_);

      // Algorithm parameters
      archiveSize_ = Integer.parseInt(configuration.getProperty("archiveSize",
            String.valueOf(archiveSize_)));
      maxEvaluations_ = Integer.parseInt(configuration.getProperty(
            "maxEvaluations", String.valueOf(maxEvaluations_)));
      biSections_ = Integer.parseInt(configuration.getProperty("biSections",
            String.valueOf(biSections_)));
      algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
      algorithm.setInputParameter("biSections", biSections_);
      algorithm.setInputParameter("archiveSize", archiveSize_);

      mutationProbability_ = Double.parseDouble(configuration.getProperty(
            "mutationProbability", String.valueOf(mutationProbability_)));
      mutationDistributionIndex_ = Double.parseDouble(configuration
            .getProperty("mutationDistributionIndex",
                  String.valueOf(mutationDistributionIndex_)));
      parameters = new HashMap();
      parameters.put("probability", mutationProbability_);
      parameters.put("distributionIndex", mutationDistributionIndex_);
      mutation = MutationFactory.getMutationOperator("PolynomialMutation",
            parameters);

      // Add the operators to the algorithm
      algorithm.addOperator("mutation", mutation);

      return algorithm;
   }
} // PAES_Settings
