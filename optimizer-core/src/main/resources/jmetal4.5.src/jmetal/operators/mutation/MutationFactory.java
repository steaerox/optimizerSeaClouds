//  MutationFactory.java
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

package jmetal.operators.mutation;

import java.util.HashMap;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.AllKindOfMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestAvailabilityAndNumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestAvailabilityMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestCostAndNumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestCostMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestInAllPACandNumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestPerformanceAndNumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.BestPerformanceMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.ChoiceBetweenBestPACandNumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.NumOfInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.RandomOfferAndInstancesMutation;
import eu.seaclouds.platform.planner.optimizer.operator.mutation.RandomOfferMutation;

/**
 * Class implementing a factory for Mutation objects.
 */
public class MutationFactory {
  
  /**
   * Gets a crossover operator through its name.
   * @param name of the operator
   * @return the operator
   * @throws JMException 
   */
  public static Mutation getMutationOperator(String name, HashMap parameters) throws JMException{
 
    if (name.equalsIgnoreCase("PolynomialMutation"))
      return new PolynomialMutation(parameters);
    else if (name.equalsIgnoreCase("BitFlipMutation"))
      return new BitFlipMutation(parameters);
    else if (name.equalsIgnoreCase("NonUniformMutation"))
      return new NonUniformMutation(parameters);
    else if (name.equalsIgnoreCase("SwapMutation"))
      return new SwapMutation(parameters);
    else if (name.equalsIgnoreCase("AllKindOfMutation"))
      return new AllKindOfMutation(parameters);
    else if (name.equalsIgnoreCase("ChoiceBetweenBestPACandNumOfInstancesMutation"))
    	return new ChoiceBetweenBestPACandNumOfInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("BestInAllPACandNumOfInstancesMutation"))
       return new BestInAllPACandNumOfInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("RandomOfferAndInstancesMutation"))
        return new RandomOfferAndInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("BestPerformanceAndNumOfInstancesMutation"))
        return new BestPerformanceAndNumOfInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("BestAvailabilityAndNumOfInstancesMutation"))
        return new BestAvailabilityAndNumOfInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("BestCostAndNumOfInstancesMutation"))
        return new BestCostAndNumOfInstancesMutation(parameters);
    else if (name.equalsIgnoreCase("BestPerformanceMutation"))
        return new BestPerformanceMutation(parameters);
    else if (name.equalsIgnoreCase("BestAvailabilityMutation"))
        return new BestAvailabilityMutation(parameters);
    else if (name.equalsIgnoreCase("BestCostMutation"))
        return new BestCostMutation(parameters);
    else if (name.equalsIgnoreCase("RandomOfferMutation"))
       return new RandomOfferMutation(parameters);
    else if (name.equalsIgnoreCase("NumOfInstancesMutation"))
        return new NumOfInstancesMutation(parameters);
    else
    {
      Configuration.logger_.severe("Operator '" + name + "' not found ");
      Class cls = java.lang.String.class;
      String name2 = cls.getName() ;    
      throw new JMException("Exception in " + name2 + ".getMutationOperator()") ;
    }        
  } // getMutationOperator
} // MutationFactory
