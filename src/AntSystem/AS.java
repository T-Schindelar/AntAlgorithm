package AntSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Ant System to solve VRPs.
 */
public class AS {
    /**
     * Q: a constant related to the quantity of trail laid by ants.
     */
    private static final double Q = 1.0;
    /**
     * The graph which represents the environment.
     */
    protected Graph graph;
    /**
     * The problem to solve.
     */
    ProblemInstance problem;
    /**
     * Number of ants.
     */
    private int numberOfAnts;
    /**
     * The ants of the Ant System.
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
     * Rho: trail persistence(1-ρ can be interpreted as trail evaporation).
     */
    private double rho = 0.1;
    /**
     * Number of Iterations.
     */
    private int numberOfIterations = 1000;
    /**
     * The position of each ant.
     */
    private int[] antPositions;

    /**
     * The best tour found.
     */
    private int[] bestTour;

    /**
     * The length of the best tour
     */
    private double bestTourLength;

    /**
     * Constructor.
     *
     * @param vertices The vertices of the graph.
     */
    public AS(Vertex[] vertices) {
        this.graph = new Graph(vertices);
        this.bestTour = new int[graph.getNumOfVertices()];
        this.numberOfAnts = graph.getNumOfVertices();
        this.ants = new Ant[numberOfAnts];
        initializeAnts();
        initializeAntPositions();
    }

    /**
     * Constructor.
     * Construct the graph and the ants and put the ants at their starting place.
     *
     * @param problem The problem to solve.
     */
    public AS(ProblemInstance problem) {
        this.problem = problem;
        this.graph = new Graph(problem.getVertices(), problem.getDemands());
        this.bestTour = new int[problem.getNumOfVertices()];
        this.numberOfAnts = problem.getNumOfVertices();
        this.ants = new Ant[numberOfAnts];
        initializeAnts();
        initializeAntPositionsAtDepot();
    }

    /**
     * Gets the value of Q.
     *
     * @return The value of Q.
     */
    public static double getQ() {
        return Q;
    }

    //todo
    /**
     * Prints the specific two dimensional matrix.
     *
     * @param matrix Matrix to print.
     */
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix)
            System.out.println(Arrays.toString(row));
        System.out.println();
    }

    /**
     * Returns a Vertex[] with n randomly distributed vertices
     * in a two dimensional grid from 0 to a specific upper bound.
     *
     * @param n  Number of vertices.
     * @param ub Upper bound for the grid.
     * @return A Vertex[] with n randomly distributed vertices.
     */
    static public Vertex[] createRandomVertices(int n, int ub) {
        Vertex[] v = new Vertex[n];
        Random rand = new Random();
        rand.setSeed(0);
        for (int i = 0; i < n; i++)
            v[i] = new Vertex(rand.nextInt(ub + 1), rand.nextInt(ub + 1));
        return v;
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
        this.beta = beta;
    }

    /**
     * Gets the value of rho
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
        if (rho < 0 || rho > 1)
            throw new IllegalArgumentException("The value of roh has to be between 0 and 1.");
        this.rho = rho;
    }

    /**
     * Sets the initial tau value for the graph.
     *
     * @param initialTau The new initial tau value.
     */
    public void setInitialTau(double initialTau) {
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
     * @return aAn int[] with the best tour.
     */
    public int[] getBestTour() {
        return bestTour;
    }

    /**
     * Gets the length of the best tour.
     *
     * @return The length of the best tour.
     */
    public double getBestTourLength() {
        return bestTourLength;
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
    public List<Integer> initializeNotVisitedVertices(int startingVertex) {
        List<Integer> notVisitedVertices = new ArrayList<>();
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
            if (bestTourLength == 0 || ant.tourLength < bestTourLength) {
                bestTour = ant.getTour();
                bestTourLength = ant.tourLength;
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
                graph.setTau(j, i, graph.getTau(i, j));

                // do deposit
                graph.setTau(i, j, graph.getTau(i, j) + getDeltaTau(i, j));
                graph.setTau(j, i, graph.getTau(i, j));
            }
        }
    }

    /**
     * Returns the computed delta tau value over all ants.
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return The delta tau value.
     */
    private double getDeltaTau(int i, int j) {
        double deltaTau = 0.0;
        for (Ant ant : ants) {
            // accumulate if the edge was used
            if (ant.path[i][j] == 1)
                deltaTau += Q / ant.tourLength;
        }
        return deltaTau;
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
}