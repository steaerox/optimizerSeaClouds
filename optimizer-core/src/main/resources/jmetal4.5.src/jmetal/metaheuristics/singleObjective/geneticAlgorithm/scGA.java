//  scGA.java
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

package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import java.util.Comparator;
import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.BestSolutionSelection;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.comparators.ObjectiveComparator;

/**
 * Class implementing a single-objective synchronous cellular genetic algorithm
 */
public class scGA extends Algorithm{

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public scGA(Problem problem){
    super(problem) ;
  } // sMOCell1


  /**   
   * Runs of the scGA algorithm.
   * @return a <code>SolutionSet</code> that contains the best found solution  
   * @throws JMException 
   */ 
  @Override
public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize, maxEvaluations, evaluations ;
    Operator mutationOperator  = null ;
    Operator crossoverOperator = null ;
    Operator selectionOperator = null ;

    SolutionSet [] neighbors;    
    SolutionSet population ;
    SolutionSet tempPopulation ;
    Neighborhood neighborhood;

    Comparator  comparator      ;
    comparator = new ObjectiveComparator(0) ; // Single objective comparator
    
    Operator findBestSolution ;
    HashMap  parameters ; // Operator parameters
    parameters = new HashMap() ;
    parameters.put("comparator", comparator) ;
    findBestSolution = new BestSolutionSelection(parameters) ;

    //Read the parameters
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                

    //Read the operators
    mutationOperator  = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");        

    //Initialize the variables    
    evaluations        = 0;                        
    neighborhood       = new Neighborhood(populationSize);
    neighbors          = new SolutionSet[populationSize];

    population = new SolutionSet(populationSize) ;
    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution solution = new Solution(problem_);
      problem_.evaluate(solution);
      problem_.evaluateConstraints(solution);
      population.add(solution);
      solution.setLocation(i);
      evaluations++;
    }         
  	
    boolean solutionFound = false ;
    while ((evaluations < maxEvaluations) && !solutionFound) {              
    	tempPopulation = new SolutionSet(populationSize);
      for (int ind = 0; ind < population.size(); ind++){      	
        Solution individual = new Solution(population.get(ind));

        Solution [] parents = new Solution[2];
        Solution [] offSpring = null ;

        neighbors[ind] = neighborhood.getEightNeighbors(population,ind);                                                           
        neighbors[ind].add(individual);

        //parents
        parents[0] = (Solution)selectionOperator.execute(neighbors[ind]);
        parents[1] = (Solution)selectionOperator.execute(neighbors[ind]);

        //Create a new solution, using genetic operators mutation and crossover
        if (crossoverOperator != null)
          offSpring = (Solution [])crossoverOperator.execute(parents);        
        else {
        	offSpring = new Solution[1] ;
        	offSpring[0] = new Solution(parents[0]) ;
        }
        mutationOperator.execute(offSpring[0]);

        //->Evaluate offspring and constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        if (comparator.compare(individual, offSpring[0]) < 0)
        	tempPopulation.add(individual) ;
        else
        	tempPopulation.add(offSpring[0]) ;     
      } // for                     
      
      population = tempPopulation;
    } // while
    
    
    SolutionSet resultPopulation = new SolutionSet(1) ;    
    resultPopulation.add(population.best(comparator));
    
    return resultPopulation ;
  } // execute        
} // scGA
