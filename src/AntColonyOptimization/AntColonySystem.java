package AntColonyOptimization;

import AntColonyOptimization.Ant.ExplorationRules.PseudoRandomExploration;
import AntColonyOptimization.Ant.SelectionRules.RouletteWheelSelection;
import AntColonyOptimization.Utilities.ProblemInstance;

/**
 * Ant Colony System algorithm to solve VRPs.
 */
public class AntColonySystem extends AntSystem {
    /**
     * Probability to select with the deterministic rule.
     */
    private double q0;

    /**
     * Constructor.
     * Construct the graph and the ants and put the ants at their starting place.
     * The value of q0 is 0.5 if not set.
     *
     * @param problem The problem to solve.
     */
    public AntColonySystem(ProblemInstance problem) {
        super(problem);
        setAntExplorationRule(new PseudoRandomExploration(new RouletteWheelSelection(), 0.5));
    }

    /**
     * Gets the value of q0.
     *
     * @return The value of q0.
     */
    public double getQ0() {
        return q0;
    }

    /**
     * Sets the value of q0.
     *
     * @param q0 The new value of q0.
     */
    public void setQ0(double q0) {
        if (q0 < 0 || q0 > 1)
            throw new IllegalArgumentException("The value of q0 has to be between 0 and 1.");
        this.q0 = q0;
        setAntExplorationRule(new PseudoRandomExploration(new RouletteWheelSelection(), q0));
    }
}