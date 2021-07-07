package AntColonyOptimization.Ant.ExplorationRules;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.Ant.SelectionRules.AntSelectionRule;

import java.util.Random;

/**
 * A pseudo random exploration.
 */
public class PseudoRandomExploration extends RandomExploration {
    /**
     * Probability to select with the deterministic rule.
     */
    private final double q0;

    /**
     * Constructor.
     *
     * @param antSelectionRule The selection rule.
     */
    public PseudoRandomExploration(AntSelectionRule antSelectionRule, double q0) {
        super(antSelectionRule);
        this.q0 = q0;
    }

    @Override
    public int selectNextVertex(Ant ant) {
        if (new Random().nextDouble() <= q0) {
            return deterministicSelection(ant);
        } else {
            return randomSelection(ant);
        }
    }

    /**
     * Selects the vertex with the highest value.
     *
     * @param ant The ant.
     * @return The next vertex.
     */
    public int deterministicSelection(Ant ant) {
        int nextNode = ant.getFeasibleVertices().get(0);
        double max = Double.MIN_VALUE;

        for (int j : ant.getFeasibleVertices()) {
            // calculate the intensity of the trail
            double tij = ant.getAco().getTau(ant.getCurrentVertex(), j);

            // calculate the visibility of the trail, quantity = 1 / d_ij
            double nij = Math.pow(1 / ant.getAco().getDistance(ant.getCurrentVertex(), j), ant.getAco().getBeta());

            double value = tij * nij;

            // select the vertex with the highest value or the first
            if (value > max) {
                max = value;
                nextNode = j;
            }
        }
        return nextNode;
    }
}