package AntSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AS {
    private static final double Q = 1.0;
    private double alpha;
    private double beta;
    private double rho;
    private final int numberOfAnts;
    private int numberOfIterations = 1;
    protected Graph graph;
    private Ant[] ants;
    private int[] antPositions;
    private int[] bestTour;
    private double bestTourValue;


    public AS(Vertex[] vertices, int numberOfAnts) {
        this.graph = new Graph(vertices);
        this.bestTour = new int[graph.getNumOfVertices()];
        this.numberOfAnts = numberOfAnts;
        initializeAnts();
        initializeAntPositions();
    }

    public static double getQ() {
        return Q;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getAntPosition(int antId) {
        return antPositions[antId];
    }


    public void printMatrix(double[][] matrix) {
        for (int i = 0; i < graph.getNumOfVertices(); i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        System.out.println();
    }

    static public Vertex[] createRandomVertices(int n) {
        Vertex[] v = new Vertex[n];
        Random rand = new Random();
        rand.setSeed(0);
        for (int i = 0; i < n; i++)
            v[i] = new Vertex(rand.nextInt(11), rand.nextInt(11));
        return v;
    }

    private void initializeAnts() {
        this.ants = new Ant[numberOfAnts];
        for (int k = 0; k < numberOfAnts; k++) {
            ants[k] = new Ant(this, k);
        }
    }

    private void initializeAntPositions() {
        antPositions = new int[numberOfAnts];
        for (int i = 0; i < numberOfAnts; i++) {
            antPositions[i] = i % graph.getNumOfVertices();
        }
    }

    public List<Integer> initializeNotVisitedVertices(int startingVertex) {
        List<Integer> notVisitedVertices = new ArrayList<>();
        for (int i = 0; i < graph.getNumOfVertices(); i++) {
            if (i != startingVertex)
                notVisitedVertices.add(i);
        }
        return notVisitedVertices;
    }

    private void buildAntsSolutions() {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfAnts);
        //Build the ant solutions by using threads
        for (Ant ant : ants) {
            executor.execute(ant);
        }
        executor.shutdown();

        //Wait until all ants finishes their tours
        while (!executor.isTerminated()){}
    }

    private void updatePheromones() {
        for (int i = 0; i < graph.getNumOfVertices(); i++) {
            for (int j = i + 1; j < graph.getNumOfVertices(); j++) {
                // Do Evaporation
                graph.setTau(i, j, (1 - rho) * getDeltaTau(i, j));
                graph.setTau(j, i, graph.getTau(i, j));

                // Do Deposit
                graph.setTau(i, j, graph.getTau(i, j) + getDeltaTau(i, j));
                graph.setTau(j, i, graph.getTau(i, j));
            }
        }
    }

    private double getDeltaTau(int i, int j) {
        double deltaTau = 0.0;
        for (Ant ant : ants) {
            if (ant.path[i][j] == 1)
                deltaTau += Q / ant.tourLength;
        }
        return deltaTau;
    }

    private void updateSolution() {
        // Update the best solution
        for (Ant ant : ants) {
            if (bestTourValue == 0 || ant.tourLength < bestTourValue) {
                bestTour = ant.getTour();
                bestTourValue = ant.tourLength;
            }
        }
    }

    public void solve() {
        for (int i = 0; i < numberOfIterations; i++) {
            buildAntsSolutions();
            updateSolution();
            updatePheromones();
        }
    }

    public static void main(String[] args) {
        AS as = new AS(createRandomVertices(10), 2);
        as.setNumberOfIterations(1000);
        as.setAlpha(1.0);
        as.setBeta(1.0);
        as.setRho(0.01);
        as.solve();
        System.out.println(Arrays.toString(as.bestTour));
        System.out.println(as.bestTourValue);
    }

}
