package AntColonyOptimization.Ant.PheromoneRules;

import AntColonyOptimization.AntColonyOptimization;

/**
 * Pheromone update rule for an ant.
 */
public abstract class AntPheromoneRule {
    /**
     * The ant colony optimization algorithm.
     */
    protected AntColonyOptimization aco;

    /**
     * Constructor.
     *
     * @param aco The ant colony optimization algorithm.
     */
    public AntPheromoneRule(AntColonyOptimization aco) {
        this.aco = aco;
    }

    /**
     * Performs the tau value update. For vertex i, j.
     *
     * @param i The current vertex.
     * @param j The next vertex.
     */
    public abstract void updateTau(int i, int j);
}