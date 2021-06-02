package AntSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AS {
    /**
     * Q: a constant related to the quantity of trail laid by ants.
     */
    private static final double Q = 1.0;

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
     * The graph which represents the environment.
     */
    protected Graph graph;

    /**
     * Number of ants.
     */
    private final int numberOfAnts;

    /**
     * The ants of the Ant System.
     */
    private final Ant[] ants;

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

    public AS(Vertex[] vertices, int numberOfAnts) {
        this.graph = new Graph(vertices);
        this.bestTour = new int[graph.getNumOfVertices()];
        this.numberOfAnts = numberOfAnts;
        this.ants = new Ant[numberOfAnts];
        initializeAnts();
        initializeAntPositions();
    }

    /**
     * @return the value of Q
     */
    public static double getQ() {
        return Q;
    }

    /**
     * @return the value of alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of alpha.
     *
     * @param alpha the value of alpha
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the value of beta
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Sets the value of beta.
     *
     * @param beta the value of beta
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    /**
     * @return the value of Q
     */
    public double getRho() {
        return rho;
    }

    /**
     * Sets the value of rho.
     *
     * @param rho the value of rho
     */
    public void setRho(double rho) {
        this.rho = rho;
    }

    /**
     * Sets the new initial tau value for the graph.
     *
     * @param initialTau the new initial tau value
     */
    public void setInitialTau(double initialTau) {
        graph.setInitialTau(initialTau);
    }



    /**
     * @return the number of iterations
     */
    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    /**
     * Sets the number of iterations.
     *
     * @param numberOfIterations the new number of iterations
     */
    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    /**
     * @param antId the id of the searched ant
     * @return the position of the ant with the given id
     */
    public int getAntPosition(int antId) {
        return antPositions[antId];
    }

    /**
     * @return an int[] with the best tour
     */
    public int[] getBestTour() {
        return bestTour;
    }

    /**
     * @return the length of the best tour
     */
    public double getBestTourLength() {
        return bestTourLength;
    }

    /**
     * Prints the given 2D matrix.
     *
     * @param matrix matrix to print
     */
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix)
            System.out.println(Arrays.toString(row));
        System.out.println();
    }

    /**
     * Returns a Vertex[] with n randomly distributed vertices in a 2D grid from 0 to upper bound.
     *
     * @param n  number of vertices
     * @param ub upper bound for the grid
     * @return a Vertex[] with n randomly distributed vertices
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
     * Initializes the ants.
     */
    private void initializeAnts() {
        for (int k = 0; k < numberOfAnts; k++)
            ants[k] = new Ant(this, k);
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
     * Initializes the not visited vertices for each ant.
     *
     * @param startingVertex the starting vertex of the ant
     * @return a list of not visited vertices for the ant
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
     * Forces every ant to construct a solution.
     */
    private void constructAntsSolutions() {
        for (Ant ant : ants)
            ant.run();
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
     * Returns the computed delta tau value oder all ants.
     *
     * @param i current vertex
     * @param j next vertex
     * @return the delta tau value
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
     * Solves the Problem.
     */
    public void solve() {
        for (int i = 0; i < numberOfIterations; i++) {
            constructAntsSolutions();
            updateSolution();
            updatePheromones();
        }
    }
}
