package AntColonyOptimization.DepositRules;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.AntColonyOptimization;

/**
 * Deposit Rule for the ant colony system.
 */
public class DepositACS extends DepositRule {
    /**
     * Constructor.
     * Only the best ant does deposit pheromones.
     *
     * @param aco The ant colony optimization algorithm.
     */
    public DepositACS(AntColonyOptimization aco) {
        super(aco, aco.getRho());
    }

    /**
     * {@inheritDoc}
     * Only best ants.
     */
    @Override
    protected double getDeltaTau(int i, int j) {
        double deltaTau = 0.0;
        // accumulate if the edge was used
        Ant ant = aco.getBestAnt();
        if (ant.getPathValue(i, j) == 1)
            deltaTau += AntColonyOptimization.Q / ant.getTourLength();
        return deltaTau;
    }
}