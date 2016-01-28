//  MultiPointCrossover.java
//
//  Author:
//       

package eu.seaclouds.platform.planner.optimizer.operator.crossover;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import eu.seaclouds.platform.planner.optimizer.CloudOffer;
import eu.seaclouds.platform.planner.optimizer.Modules;
import eu.seaclouds.platform.planner.optimizer.SeaCloudsType;

/**
 * This class allows to apply a Multi Point crossover operator using two parent
 * solutions.
 */
public class MultiPointCrossover extends Crossover {
    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays
            .asList(SeaCloudsType.class);

    private Double crossoverProbability_ = null;

    /**
     * Constructor Creates a new instance of the multi point crossover operator
     */
    public MultiPointCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("probability") != null)
            crossoverProbability_ = (Double) parameters.get("probability");
    } // SinglePointCrossover

    /**
     * Perform the crossover operation.
     * 
     * @param probability
     *            Crossover probability
     * @param parent1
     *            The first parent
     * @param parent2
     *            The second parent
     * @return An array containig the two offsprings
     * @throws JMException
     */
    public Solution[] doCrossover(double probability, Solution parent1,
            Solution parent2) throws JMException {
        Solution[] offSpring = new Solution[2];
        offSpring[0] = new Solution(parent1);
        offSpring[1] = new Solution(parent2);
        try {

            if (parent1.getType().getClass() == SeaCloudsType.class) {

                int numOfModules = ((Modules) parent1.getDecisionVariables()[0])
                        .getModuleList().size();

                for (int i = 0; i < numOfModules; i++) {
                    if (((Modules) parent1.getDecisionVariables()[0])
                            .getModuleList().get(i).isComputable()) {
                        // Try a Random probability for each Computable Module
                        if (PseudoRandom.randDouble() < probability) {
                            // Saving temp CloudOffer to swap between same
                            // Module (different chromosome)
                            CloudOffer temp1 = ((Modules) parent1
                                    .getDecisionVariables()[0]).getModuleList()
                                    .get(i).getBestCloudOffer();
                            int tempNumOfInstances1 = ((Modules) parent1
                                    .getDecisionVariables()[0]).getModuleList()
                                    .get(i)
                                    .getNumOfInstancesForBestCloudOffer();
                            CloudOffer temp2 = ((Modules) parent2
                                    .getDecisionVariables()[0]).getModuleList()
                                    .get(i).getBestCloudOffer();
                            int tempNumOfInstances2 = ((Modules) parent2
                                    .getDecisionVariables()[0]).getModuleList()
                                    .get(i)
                                    .getNumOfInstancesForBestCloudOffer();
                            ((Modules) offSpring[0].getDecisionVariables()[0])
                                    .getModuleList().get(i)
                                    .setBestCloudOffer(temp2);
                            ((Modules) offSpring[0].getDecisionVariables()[0])
                                    .getModuleList()
                                    .get(i)
                                    .setNumOfInstancesForBestCloudOffer(
                                            tempNumOfInstances2);
                            ((Modules) offSpring[1].getDecisionVariables()[0])
                                    .getModuleList().get(i)
                                    .setBestCloudOffer(temp1);
                            ((Modules) offSpring[1].getDecisionVariables()[0])
                                    .getModuleList()
                                    .get(i)
                                    .setNumOfInstancesForBestCloudOffer(
                                            tempNumOfInstances1);
                        }
                    }
                }
                
                ((Modules) offSpring[0].getDecisionVariables()[0]).computeGlobalQoS();
                ((Modules) offSpring[0].getDecisionVariables()[0]).computeConstraints(((Modules) offSpring[0].getDecisionVariables()[0]).getConstraints().length);
                ((Modules) offSpring[1].getDecisionVariables()[0]).computeGlobalQoS();
                ((Modules) offSpring[1].getDecisionVariables()[0]).computeConstraints(((Modules) offSpring[1].getDecisionVariables()[0]).getConstraints().length);
                
            }

        } catch (ClassCastException e1) {
            Configuration.logger_
                    .severe("MultiPointCrossover.doCrossover: Cannot perfom "
                            + "MultiPointCrossover");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doCrossover()");
        }
        return offSpring;
    } // doCrossover

    /**
     * Executes the operation
     * 
     * @param object
     *            An object containing an array of two solutions
     * @return An object containing an array with the offSprings
     * @throws JMException
     */
    @Override
    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;

        if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES
                .contains(parents[1].getType().getClass()))) {

            Configuration.logger_
                    .severe("MultiPointCrossover.execute: the solutions "
                            + "are not of the right type. The type should be 'SeaCloudsType', but "
                            + parents[0].getType() + " and "
                            + parents[1].getType() + " are obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        if (parents.length < 2) {
            Configuration.logger_
                    .severe("MultiPointCrossover.execute: operator "
                            + "needs two parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        Solution[] offSpring;
        offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);

        // -> Update the offSpring solutions
        for (int i = 0; i < offSpring.length; i++) {
            offSpring[i].setCrowdingDistance(0.0);
            offSpring[i].setRank(0);
        }
        return offSpring;
    } // execute
} // MultiPointCrossover
