package AntSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant implements Runnable {
    protected int id;
    public AS as;
    public int currentVertex;
    public int[][] path;
    private final List<Integer> tour;
    public List<Integer> notVisitedVertices;
    protected double tourLength;

    public Ant(AS as, int id) {
        this.id = id;
        this.as = as;
        this.notVisitedVertices = new ArrayList<>();
        this.tour = new ArrayList<>();
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
    }

    public void reset() {
        this.currentVertex = as.getAntPosition(id);
        this.tourLength = 0.0;
        this.tour.clear();
        this.notVisitedVertices = as.initializeNotVisitedVertices(currentVertex);
        this.tour.add(currentVertex);
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
    }

    public void explore() {
        // The search ends when their is no more vertex to visit
        while (!notVisitedVertices.isEmpty()) {
            // Get the next node given the current node
            int nextVertex = selectNextVertex();

            // Remove the next node from the list of nodes to visit
            notVisitedVertices.remove((Integer) nextVertex);

            // Save the next node in the tour
            tour.add(nextVertex);

            // Mark as visited the arc(i,j)
            path[currentVertex][nextVertex] = 1;
            path[nextVertex][currentVertex] = 1;


            // Define the next node as current node
            currentVertex = nextVertex;
        }
        computeTourLength();
    }

    private void computeTourLength() {
        for (int i = 0; i < tour.size() - 1; i++) {
            tourLength += as.graph.getDistance(i, i + 1);
        }
    }

    private int selectNextVertex() {
        double sum = 0.0;

        double[] tij = new double[as.graph.getNumOfVertices()];     // pheromone intensity
        double[] nij = new double[as.graph.getNumOfVertices()];     // visibility
        // Update the sum
        for (int j : notVisitedVertices) {
            tij[j] = Math.pow(as.graph.getTau(currentVertex, j), as.getAlpha());
            nij[j] = Math.pow(as.graph.getTau(currentVertex, j), as.getBeta());
            sum += tij[j] * nij[j];
        }

        double[] probability = new double[as.graph.getNumOfVertices()];
        double sumProbability = 0.0;
        for (Integer j : notVisitedVertices) {
            probability[j] = (tij[j] * nij[j]) / sum;
            sumProbability += probability[j];
        }

        // Select the next vertex by probability
        return rouletteWheelSelection(probability, sumProbability);
    }

    private int rouletteWheelSelection(double[] probability, double sumProbability) {
        //todo rand.nextDouble(), wenn seed nicht mehr benötigt
        Random rand = new Random();
        rand.setSeed(2);
        double randNum = rand.nextDouble() * sumProbability;    // random number in range 0...sumProbability
        int selection = 0;
        double accumulatedProbability = probability[selection];
        while (accumulatedProbability < randNum) {
            selection++;
            accumulatedProbability += probability[selection];
        }
        return selection;
    }

    public int[] getTour() {
        return tour.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public void run() {
        reset();
        explore();
    }
}
