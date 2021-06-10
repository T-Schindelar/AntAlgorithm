package AntColonyOptimization.Ant.SelectionRules;

/**
 * Selection rule for the ant.
 */
public abstract class AntSelectionRule {
    /**
     * Returns an integer which represents the selected vertex.
     *
     * @param probability    A double array with the probability of each vertex.
     * @param sumProbability The sum of the probabilities.
     * @return The selected vertex.
     */
    public abstract int select(double[] probability, double sumProbability);
}