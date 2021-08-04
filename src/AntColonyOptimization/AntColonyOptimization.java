package AntColonyOptimization;

import AntColonyOptimization.Ant.Ant;
import AntColonyOptimization.Ant.ExplorationRules.AntExplorationRule;
import AntColonyOptimization.Ant.PheromoneRules.AntPheromoneRule;
import AntColonyOptimization.DepositRules.DepositRule;
import Graph.Graph;
import Utilities.ProblemInstance;

import java.util.ArrayList;

/**
 * Ant Colony Optimization algorithm to solve CVRPs.
 */
public abstract class AntColonyOptimization {
    /**
     * Q: a constant related to the quantity of trail laid by ants.
     */
    public static final double Q = 1.0;
    /**
     * The graph which represents the environment.
     */
    private final Graph graph;
    /**
     * The problem to solve.
     */
    ProblemInstance problem;
    /**
     * Number of ants.
     */
    private int numberOfAnts;
    /**
     * The ants of the System.
     */
    private Ant[] ants;
    /**
     * Alpha: the relative importance of the trail.
     */
    private double alpha = 1.0;
    /**
     * Beta: the relative importance of the visibility.
     */
    private double beta = 1.0;
    /**
     * Rho: trail persistence (1 - ρ can be interpreted as trail evaporation).
     */
    private double rho = 0.1;
    /**
     * Exploration rule of the ant.
     */
    private AntExplorationRule antExplorationRule;
    /**
     * The ant deposit rule
     */
    private AntPheromoneRule antPheromoneRule;
    /**
     * Deposit rule of the system to update the pheromones.
     */
    private DepositRule depositRule;
    /**
     * Number of Iterations.
     */
    private int numberOfIterations = 1000;
    /**
     * The position of each ant.
     */
    private int[] antPositions;

    /**
     * The ant with the best tour.
     */
    private Ant bestAnt;

    /**
     * Constructor.
     * Construct the graph and the ants and put the ants at their starting place.
     *
     * @param problem The problem to solve.
     */
    public AntColonyOptimization(ProblemInstance problem) {
        this.problem = problem;
        this.graph = new Graph(problem.getVertices(), problem.getDemands());
        this.numberOfAnts = problem.getNumOfVertices();
        this.ants = new Ant[numberOfAnts];
        initializeAnts();
        initializeAntPositionsAtDepot();
    }

    /**
     * Solve the Problem.
     */
    public void solve() {
        for (int i = 0; i < numberOfIterations; i++) {
            constructAntsSolutions();
            updateSolution();
            updatePheromones();
        }
    }

    /**
     * Initializes the ants.
     */
    private void initializeAnts() {
        for (int id = 0; id < numberOfAnts; id++)
            ants[id] = new Ant(this, id, problem.getVehicleCapacity());
    }

    /**
     * Initializes the starting position for each ant.
     */
    private void initializeAntPositions() {
        antPositions = new int[numberOfAnts];
        for (int i = 0; i < numberOfAnts; i++)
            antPositions[i] = i % graph.getNumOfVertices();
    }

    /**
     * Initializes the starting position for each ant at the Depot.
     */
    private void initializeAntPositionsAtDepot() {
        antPositions = new int[numberOfAnts];
        for (int i = 0; i < numberOfAnts; i++)
            antPositions[i] = 0;
    }

    /**
     * Initializes the not visited vertices for each ant.
     *
     * @param startingVertex The starting vertex of the ant.
     * @return A list of not visited vertices for the ant.
     */
    public ArrayList<Integer> initializeNotVisitedVertices(int startingVertex) {
        ArrayList<Integer> notVisitedVertices = new ArrayList<>();
        for (int i = 0; i < graph.getNumOfVertices(); i++) {
            if (i != startingVertex)
                notVisitedVertices.add(i);
        }
        return notVisitedVertices;
    }

    /**
     * Construct solutions.
     */
    private void constructAntsSolutions() {
        for (Ant ant : ants)
            ant.run();
    }

    /**
     * Updates the best solution.
     */
    private void updateSolution() {
        for (Ant ant : ants) {
            if (bestAnt == null || ant.getTourLength() < bestAnt.getTourLength()) {
                bestAnt = ant.clone();
            }
        }
    }

    /**
     * Updates the pheromones.
     */
    private void updatePheromones() {
        for (int i = 0; i < graph.getNumOfVertices(); i++) {
            for (int j = i + 1; j < graph.getNumOfVertices(); j++) {
                // do evaporation
                graph.setTau(i, j, (1 - rho) * graph.getTau(i, j));

                // do deposit
                graph.setTau(i, j, depositRule.getNewTau(i, j));

                // set symmetric tau
                graph.setTau(j, i, graph.getTau(i, j));
            }
        }
    }

    /**
     * Gets the value of alpha.
     *
     * @return The value of alpha.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of alpha.
     *
     * @param alpha The new value of alpha.
     */
    public void setAlpha(double alpha) {
        if (alpha < 0)
            throw new IllegalArgumentException("The value of alpha has to be greater than 0");
        this.alpha = alpha;
    }

    /**
     * Gets the value of beta.
     *
     * @return The value of beta.
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Sets the value of beta.
     *
     * @param beta The new value of beta.
     */
    public void setBeta(double beta) {
        if (beta < 0)
            throw new IllegalArgumentException("The value of beta has to be greater than 0");
        this.beta = beta;
    }

    /**
     * Gets the value of rho.
     *
     * @return The value of rho.
     */
    public double getRho() {
        return rho;
    }

    /**
     * Sets the value of rho.
     *
     * @param rho The new value of rho.
     */
    public void setRho(double rho) {
        if (rho < 0 || rho >= 1)
            throw new IllegalArgumentException("The value of rho has to be between 0 and 1, including 0.");
        this.rho = rho;
    }

    /**
     * Gets the initial tau value of the graph.
     *
     * @return The value of the initial tau of the graph.
     */
    public double getInitialTau() {
        return graph.getInitialTau();
    }

    /**
     * Sets the initial tau value of the graph.
     *
     * @param initialTau The new initial tau value.
     */
    public void setInitialTau(double initialTau) {
        if (initialTau <= 0)
            throw new IllegalArgumentException("The initial tau has to be greater than 0");
        graph.setInitialTau(initialTau);
    }

    /**
     * Gets the number of iterations.
     *
     * @return The number of iterations.
     */
    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    /**
     * Sets the number of iterations.
     *
     * @param numberOfIterations The new number of iterations.
     */
    public void setNumberOfIterations(int numberOfIterations) {
        if (numberOfIterations < 0)
            throw new IllegalArgumentException("The number of iterations has to be greater than 0");
        this.numberOfIterations = numberOfIterations;
    }

    /**
     * Gets the number of ants.
     *
     * @return The number of ants.
     */
    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    /**
     * Sets the number of ants.
     * Initializes the ants and puts them at their starting vertex.
     *
     * @param numberOfAnts The new number of ants.
     */
    public void setNumberOfAnts(int numberOfAnts) {
        if (numberOfAnts < 0)
            throw new IllegalArgumentException("The number of ants has to be greater than 0");
        this.numberOfAnts = numberOfAnts;
        ants = new Ant[numberOfAnts];
        initializeAnts();
        initializeAntPositionsAtDepot();
    }

    /**
     * Gets the position of the ant with the specific id.
     *
     * @param antId The id of the searched ant.
     * @return The position of the ant with the specific id.
     */
    public int getAntPosition(int antId) {
        return antPositions[antId];
    }

    /**
     * Gets the best tour.
     *
     * @return An int[] with the best tour.
     */
    public int[] getBestTour() {
        return bestAnt.getTour();
    }

    /**
     * Gets the length of the best tour.
     *
     * @return The length of the best tour.
     */
    public double getBestTourLength() {
        return bestAnt.getTourLength();
    }

    /**
     * Gets the number of vertices of the graph.
     *
     * @return Number of vertices of the graph.
     */
    public int getNumOfVertices() {
        return graph.getNumOfVertices();
    }

    /**
     * Gets the distance for the edge(i,j).
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return Distance for the edge(i,j).
     */
    public double getDistance(int i, int j) {
        return graph.getDistance(i, j);
    }

    /**
     * Gets the pheromone value of the edge(i,j).
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return Pheromone value of the edge(i,j).
     */
    public double getTau(int i, int j) {
        return graph.getTau(i, j);
    }

    /**
     * Sets the tau value of the edge(i,j).
     *
     * @param i     The current vertex.
     * @param j     The next vertex.
     * @param value The new pheromone value of the edge(i,j).
     */
    public void setTau(int i, int j, double value) {
        graph.setTau(i, j, value);
    }

    /**
     * Gets the demand of a vertex.
     *
     * @param i The vertex.
     * @return Demand of vertex i.
     */
    public int getDemands(int i) {
        return graph.getDemands(i);
    }

    /**
     * Gets the ant exploration rule.
     *
     * @return The specific exploration rule.
     */
    public AntExplorationRule getAntExplorationRule() {
        return antExplorationRule;
    }

    /**
     * Sets the ant exploration rule.
     *
     * @param antExplorationRule The new exploration rule.
     */
    public void setAntExplorationRule(AntExplorationRule antExplorationRule) {
        this.antExplorationRule = antExplorationRule;
    }

    /**
     * Gets the pheromone update rule for an ant.
     *
     * @return The specific pheromone rule.
     */
    public AntPheromoneRule getAntPheromoneRule() {
        return antPheromoneRule;
    }

    /**
     * Sets the pheromone update rule for an ant.
     *
     * @param antPheromoneRule The specific pheromone rule.
     */
    public void setAntPheromoneRule(AntPheromoneRule antPheromoneRule) {
        this.antPheromoneRule = antPheromoneRule;
    }

    /**
     * Gets the deposit rule of the system.
     *
     * @return The specific deposit rule.
     */
    public DepositRule getDepositRule() {
        return depositRule;
    }

    /**
     * Sets the deposit rule of the system.
     *
     * @param depositRule The new deposit rule.
     */
    public void setDepositRule(DepositRule depositRule) {
        this.depositRule = depositRule;
    }

    /**
     * Gets the best ant.
     *
     * @return The best ant.
     */
    public Ant getBestAnt() {
        return bestAnt;
    }

    /**
     * Gets the ants.
     *
     * @return The ants of the system.
     */
    public Ant[] getAnts() {
        return ants;
    }
}