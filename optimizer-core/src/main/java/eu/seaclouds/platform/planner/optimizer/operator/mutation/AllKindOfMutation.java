//  SwapMutation.java
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

package eu.seaclouds.platform.planner.optimizer.operator.mutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import eu.seaclouds.platform.planner.optimizer.Modules;
import eu.seaclouds.platform.planner.optimizer.SeaCloudsType;

/**
 * This class implements a Random mutation of each Variable probabilistically.
 * The solution type of the solution must be SeaClouds.
 */
public class AllKindOfMutation extends Mutation {
    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays
            .asList(SeaCloudsType.class);

    private Double mutationProbability_ = null;

    /**
     * Constructor
     */
    public AllKindOfMutation(
            HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get("probability") != null)
            mutationProbability_ = (Double) parameters.get("probability");
    } // Constructor

    /**
     * Performs the operation
     * 
     * @param probability
     *            Mutation probability
     * @param solution
     *            The solution to mutate
     * @throws JMException
     */
    public void doMutation(double probability, Solution solution)
            throws JMException {

        if (solution.getType().getClass() == SeaCloudsType.class) {

            int length = ((Modules) solution.getDecisionVariables()[0])
                    .getModuleList().size();

            for (int i = 0; i < length; i++) {
                if (((Modules) solution.getDecisionVariables()[0])
                        .getModuleList().get(i).isComputable()) {
                    if (PseudoRandom.randDouble() < probability) {
                       
                       
                       int switcher = PseudoRandom.randInt(1, 11);

                       switch (switcher) {
                       case 1:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i)
                          .setBestAvailabilityCloudOfferAndNewInstances();
                          break;
                       case 2:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i)
                          .setBestAvailabilityCloudOffer();
                          break;
                       case 3:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i).setBestCostCloudOfferAndNewInstances();
                          break;
                       case 4:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i).setBestCostCloudOffer();
                          break;
                       case 5:
                          ((Modules) solution.getDecisionVariables()[0]).getModuleList().get(i).setBestPACandNumOfInstances(true);
                          break;
                       case 6:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i)
                          .setBestPerformanceCloudOfferAndNewInstances();
                          break;
                       case 7:
                          ((Modules) solution.getDecisionVariables()[0])
                          .getModuleList().get(i).setBestPerformanceCloudOffer();
                          break;
                       case 8:
                          ((Modules) solution.getDecisionVariables()[0]).getModuleList().get(i).setBestCloudOfferBetweenPACequallyRandom();
                          break;
                       case 9:
                          ((Modules) solution
                                .getDecisionVariables()[0]).getModuleList()
                                .get(i).setNewInstancesForCurrentBestCloudOffer();
                          break;
                       case 10:
                          ((Modules) solution
                                .getDecisionVariables()[0]).getModuleList()
                                .get(i).setRandomOffer(true);
                          break;
                       case 11:
                          ((Modules) solution
                                .getDecisionVariables()[0]).getModuleList()
                                .get(i).setRandomOffer(false);
                          break;
                       default:
                          break;
                       }
                       
                       

                    } // if
                }
            }

            ((Modules) solution.getDecisionVariables()[0]).computeGlobalQoS();
            ((Modules) solution.getDecisionVariables()[0]).computeConstraints(((Modules) solution.getDecisionVariables()[0]).getConstraints().length);
            
            // Module[] variables = (Module[]) solution.getDecisionVariables();
            // int length = variables.length;
            // for (int i = 0; i < length; i++) {
            // if (((Module) solution.getDecisionVariables()[i])
            // .isComputable()) {
            // if (PseudoRandom.randDouble() < probability) {
            //
            // int numOfInstances = PseudoRandom
            // .randInt(0, ((Module) solution
            // .getDecisionVariables()[i]).DEFAULT_MAX_NUM_OF_INSTANCES);
            //
            // ((Module) solution.getDecisionVariables()[i])
            // .setNumOfInstancesForBestCloudOffer(numOfInstances);
            // ((Module)
            // solution.getDecisionVariables()[i]).setBestAvailabilityCloudOffer();
            //
            // } // if
            // }
            // }
        } // if
        else {
            Configuration.logger_
                    .severe("RandomOfferMutation.doMutation: invalid type. "
                            + ""
                            + solution.getDecisionVariables()[0]
                                    .getVariableType());

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doMutation()");
        }
    } // doMutation

    /**
     * Executes the operation
     * 
     * @param object
     *            An object containing the solution to mutate
     * @return an object containing the mutated solution
     * @throws JMException
     */
    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_
                    .severe("RandomOfferMutation.execute: the solution "
                            + "is not of the right type. The type should be 'SeaCloudsTypeNewModules', but "
                            + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        this.doMutation(mutationProbability_, solution);
        return solution;
    } // execute
} // SwapMutation
