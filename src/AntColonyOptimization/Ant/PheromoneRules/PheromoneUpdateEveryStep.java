package AntColonyOptimization.Ant.PheromoneRules;

import AntColonyOptimization.AntColonyOptimization;

/**
 * Pheromone update at every step of an ant.
 */
public class PheromoneUpdateEveryStep extends AntPheromoneRule {
    /**
     * Constructor.
     *
     * @param aco The ant colony optimization algorithm.
     */
    public PheromoneUpdateEveryStep(AntColonyOptimization aco) {
        super(aco);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTau(int i, int j) {
        // calculate and set tau
        aco.setTau(i, j, (1 - aco.getRho()) * aco.getTau(i, j) + aco.getRho() * aco.getInitialTau());

        // set symmetric tau
        aco.setTau(j, i, aco.getTau(i, j));
    }
}