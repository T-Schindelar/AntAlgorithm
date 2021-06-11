package AntColonyOptimization;

import AntColonyOptimization.Ant.ExplorationRules.RandomExploration;
import AntColonyOptimization.Ant.SelectionRules.RouletteWheelSelection;
import AntColonyOptimization.DepositRules.DepositAS;
import Utilities.ProblemInstance;

/**
 * Ant System algorithm to solve VRPs.
 */
public class AntSystem extends AntColonyOptimization {
    /**
     * Constructor.
     * Construct the graph and the ants and put the ants at their starting place.
     *
     * @param problem The problem to solve.
     */
    public AntSystem(ProblemInstance problem) {
        super(problem);
        setAntExplorationRule(new RandomExploration(new RouletteWheelSelection()));
        setDepositRule(new DepositAS(this));
    }
}