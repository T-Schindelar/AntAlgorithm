package AntColonyOptimization.DepositRules;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.AntColonyOptimization;

/**
 * Deposit Rule for the ant system.
 */
public class DepositAS extends DepositRule {
    /**
     * Constructor.
     * All ants do deposit pheromones.
     *
     * @param aco The ant colony optimization algorithm.
     */
    public DepositAS(AntColonyOptimization aco) {
        super(aco, 1);
    }

    /**
     * {@inheritDoc}
     * Over all ants.
     */
    @Override
    protected double getDeltaTau(int i, int j) {
        double deltaTau = 0.0;
        // accumulate if the edge was used
        for (Ant ant : aco.getAnts())
            if (ant.getPathValue(i, j) == 1)
                deltaTau += AntColonyOptimization.Q / ant.getTourLength();
        return deltaTau;
    }
}