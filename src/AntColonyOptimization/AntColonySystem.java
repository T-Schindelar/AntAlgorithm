package AntColonyOptimization;

import AntColonyOptimization.Ant.ExplorationRules.PseudoRandomExploration;
import AntColonyOptimization.Ant.PheromoneRules.PheromoneUpdateEveryStep;
import AntColonyOptimization.Ant.SelectionRules.RouletteWheelSelection;
import AntColonyOptimization.DepositRules.DepositACS;
import Utilities.ProblemInstance;

/**
 * Ant Colony System algorithm to solve VRPs.
 */
public class AntColonySystem extends AntSystem {
    /**
     * Probability to select with the deterministic rule.
     */
    private double q0;
    /**
     * Phi: evaporation after each step of an ant.
     */
    private double phi;

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
        setAntPheromoneRule(new PheromoneUpdateEveryStep(this, 0.1));
        setDepositRule(new DepositACS(this));
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

    /**
     * Gets the value of phi.
     *
     * @return The value of phi.
     */
    public double getPhi() {
        return phi;
    }

    /**
     * Sets the value of phi.
     *
     * @param phi The new value of phi.
     */
    public void setPhi(double phi) {
        if (phi < 0 || phi >= 1)
            throw new IllegalArgumentException("The value of phi has to be between 0 and 1.");
        this.phi = phi;
        setAntPheromoneRule(new PheromoneUpdateEveryStep(this, phi));
    }
}