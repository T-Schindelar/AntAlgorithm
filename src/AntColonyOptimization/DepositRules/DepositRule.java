package AntColonyOptimization.DepositRules;

import AntColonyOptimization.AntColonyOptimization;

/**
 * Deposit rules of the ant colony optimization algorithm.
 */
public abstract class DepositRule {
    /**
     * The ant colony optimization algorithm.
     */
    protected AntColonyOptimization aco;
    /**
     * The rho value for partial deposit.
     */
    private final double rho;

    /**
     * Constructor.
     *
     * @param aco The ant colony optimization algorithm.
     * @param rho The rho value for partial deposit.
     */
    public DepositRule(AntColonyOptimization aco, double rho) {
        this.aco = aco;
        this.rho = rho;
    }

    /**
     * Gets the new tau value of vertex i, j.
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return The new tau value.
     */
    public double getNewTau(int i, int j) {
        return aco.getTau(i, j) + rho * getDeltaTau(i, j);
    }

    /**
     * Returns the computed delta tau value.
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return The delta tau value.
     */
    protected abstract double getDeltaTau(int i, int j);
}