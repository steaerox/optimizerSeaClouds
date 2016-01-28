//  RandomOfferMutation.java
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
public class RandomOfferMutation extends Mutation {
    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays
            .asList(SeaCloudsType.class);

    private Double mutationProbability_ = null;

    /**
     * Constructor
     */
    public RandomOfferMutation(HashMap<String, Object> parameters) {
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

            // Module[] variables = (Module[]) solution.getDecisionVariables();
            // int length = variables.length;

            for (int i = 0; i < length; i++) {
                if (((Modules) solution.getDecisionVariables()[0])
                        .getModuleList().get(i).isComputable()) {
                    // Try a Random probability for each Computable Module
                    if (PseudoRandom.randDouble() < probability) {
                       
                       ((Modules) solution
                             .getDecisionVariables()[0]).getModuleList()
                             .get(i).setRandomOffer(false);
                       
//                        int currentBestIndex = ((Modules) solution
//                                .getDecisionVariables()[0]).getModuleList()
//                                .get(i).getBestCloudOfferIndex();
//                        int index = PseudoRandom.randInt(0,
//                                ((Modules) solution.getDecisionVariables()[0])
//                                        .getModuleList().get(i)
//                                        .getSuitableCloudOfferList().size() - 1);
//
//                        while (currentBestIndex == index) {
//                            index = PseudoRandom
//                                    .randInt(0, ((Modules) solution
//                                            .getDecisionVariables()[0])
//                                            .getModuleList().get(i)
//                                            .getSuitableCloudOfferList().size() - 1);
//                        }
//
//                        ((Modules) solution.getDecisionVariables()[0])
//                                .getModuleList().get(i)
//                                .setBestCloudOfferByIndex(index);
                        
                        
                    } // if
                }
            }

            ((Modules) solution.getDecisionVariables()[0]).computeGlobalQoS();
            ((Modules) solution.getDecisionVariables()[0]).computeConstraints(((Modules) solution.getDecisionVariables()[0]).getConstraints().length);
            
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
} // RandomOfferMutation
