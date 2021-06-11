package AntColonyOptimization.Ant.PheromoneRules;

import AntColonyOptimization.AntColonyOptimization;

/**
 * Pheromone update at every step of an ant.
 */
public class PheromoneUpdateEveryStep extends AntPheromoneRule {
    /**
     * Phi: evaporation after each step of an ant.
     */
    private final double phi;

    /**
     * /**
     * Constructor.
     *
     * @param aco The ant colony optimization algorithm.
     * @param phi The value of phi.
     */
    public PheromoneUpdateEveryStep(AntColonyOptimization aco, double phi) {
        super(aco);
        this.phi = phi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTau(int i, int j) {
        // calculate and set tau
        aco.setTau(i, j, (1 - phi) * aco.getTau(i, j) + phi * aco.getInitialTau());

        // set symmetric tau
        aco.setTau(j, i, aco.getTau(i, j));
    }
}