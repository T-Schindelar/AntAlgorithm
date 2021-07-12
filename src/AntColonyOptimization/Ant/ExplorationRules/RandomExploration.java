package AntColonyOptimization.Ant.ExplorationRules;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.Ant.SelectionRules.AntSelectionRule;

/**
 * {@inheritDoc}
 * Explores randomly.
 */
public class RandomExploration extends AntExplorationRule {
    /**
     * Constructor.
     *
     * @param antSelectionRule The selection rule.
     */
    public RandomExploration(AntSelectionRule antSelectionRule) {
        super(antSelectionRule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectNextVertex(Ant ant) {
        return randomSelection(ant);
    }

    /**
     * Selects randomly the next vertex.
     * Takes the probability of each feasible vertex into account.
     *
     * @param ant The ant.
     * @return The next vertex.
     */
    public int randomSelection(Ant ant) {
        double[] tij = new double[ant.getAco().getNumOfVertices()];     // pheromone intensity
        double[] nij = new double[ant.getAco().getNumOfVertices()];     // visibility

        double sum = 0.0;   // sum of tij * nij
        // update the sum
        for (int j : ant.getFeasibleVertices()) {
            // calculate the intensity of the trail
            tij[j] = Math.pow(ant.getAco().getTau(ant.getCurrentVertex(), j), ant.getAco().getAlpha());

            // calculate the visibility of the trail, quantity = 1 / d_ij
            nij[j] = Math.pow(1 / ant.getAco().getDistance(ant.getCurrentVertex(), j), ant.getAco().getBeta());

            sum += tij[j] * nij[j];
        }

        // compute the probabilities
        double[] probability = new double[ant.getAco().getNumOfVertices()];
        double sumProbability = 0.0;
        for (int j : ant.getFeasibleVertices()) {
            probability[j] = (tij[j] * nij[j]) / sum;
            sumProbability += probability[j];
        }

        // select the next vertex
        return antSelectionRule.select(probability, sumProbability);
    }
}