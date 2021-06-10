package AntColonyOptimization.Ant.SelectionRules;

import java.util.Random;

/**
 * Roulette Wheel Selection for the next vertex.
 */
public class RouletteWheelSelection extends AntSelectionRule {
    /**
     * {@inheritDoc}
     * Selects randomly depending on the probability.
     */
    @Override
    public int select(double[] probability, double sumProbability) {
        // random number in range 0...sumProbability
        double randNum = new Random().nextDouble() * sumProbability;

        int selection = 0;
        double accumulatedProbability = probability[selection];
        while (accumulatedProbability < randNum) {
            selection++;
            accumulatedProbability += probability[selection];
        }
        return selection;
    }
}