//  NumOfInstancesMutation.java
//
//  Author:
//       

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
 * This class implements a Random mutation of Cloud offer's number of Instances
 * of each Variable probabilistically. The solution type of the solution must be
 * SeaClouds.
 */
public class NumOfInstancesMutation extends Mutation {
    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays
            .asList(SeaCloudsType.class);

    private Double mutationProbability_ = null;

    /**
     * Constructor
     */
    public NumOfInstancesMutation(HashMap<String, Object> parameters) {
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

                       ((Modules) solution
                             .getDecisionVariables()[0]).getModuleList()
                             .get(i).setNewInstancesForCurrentBestCloudOffer();
                       
                       
//                        int numOfInstances = PseudoRandom
//                                .randInt(
//                                        ((Modules) solution
//                                                .getDecisionVariables()[0])
//                                                .getModuleList()
//                                                .get(i)
//                                                .getNumOfInstancesForBestCloudOffer(),
//                                        ((Modules) solution
//                                                .getDecisionVariables()[0])
//                                                .getModuleList().get(i).DEFAULT_MAX_NUM_OF_INSTANCES);
//
//                        ((Modules) solution.getDecisionVariables()[0])
//                                .getModuleList()
//                                .get(i)
//                                .setNumOfInstancesForBestCloudOffer(
//                                        numOfInstances);
                        
                    } // if
                }
            }

            ((Modules) solution.getDecisionVariables()[0]).computeGlobalQoS();
            ((Modules) solution.getDecisionVariables()[0]).computeConstraints(((Modules) solution.getDecisionVariables()[0]).getConstraints().length);
            
            //
            // Module[] variables = (Module[]) solution.getDecisionVariables();
            // int length = variables.length;
            //
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
            // } // if
            // }
            // }

        } // if
        else {
            Configuration.logger_
                    .severe("NumOfInstancesMutation.doMutation: invalid type. "
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
                    .severe("NumOfInstancesMutation.execute: the solution "
                            + "is not of the right type. The type should be 'SeaCloudsTypeNewModules', but "
                            + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        this.doMutation(mutationProbability_, solution);
        return solution;
    } // execute
} // NumOfInstancesMutation
