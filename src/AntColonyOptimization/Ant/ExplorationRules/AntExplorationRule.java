package AntColonyOptimization.Ant.ExplorationRules;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.Ant.SelectionRules.AntSelectionRule;

/**
 * Exploration rule for the ant. Describes how the ant explores.
 */
public abstract class AntExplorationRule {
    /**
     * Selection rule for the exploration of the ant.
     */
    protected AntSelectionRule antSelectionRule;

    /**
     * Constructor.
     *
     * @param antSelectionRule The ant selection rule.
     */
    public AntExplorationRule(AntSelectionRule antSelectionRule) {
        this.antSelectionRule = antSelectionRule;
    }

    /**
     * Returns an integer which represents the next selected vertex from the not visited vertices list of this ant.
     * Considering the demands and the capacity.
     *
     * @param ant The ant.
     * @return The next vertex.
     */
    public abstract int selectNextVertex(Ant ant);
}