package AntSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ant for the Ant System.
 */
public class Ant implements Runnable {
    /**
     * The created Tour of the ant.
     */
    private final List<Integer> tour;

    /**
     * The Ant System (AS) in which the ant lives.
     */
    public AS as;

    /**
     * Current vertex.
     */
    public int currentVertex;

    /**
     * The List of not visited vertices.
     */
    public List<Integer> notVisitedVertices;

    /**
     * The traveled path of the ant.
     */
    public int[][] path;

    /**
     * Identifier for the ant.
     */
    protected int id;

    /**
     * The lenght of the tour.
     */
    protected double tourLength;

    /**
     * Constructor.
     *
     * @param as The instance of the Ant System (AS) in which the ant lives.
     * @param id The id for the ant.
     */
    public Ant(AS as, int id) {
        this.id = id;
        this.as = as;
        this.notVisitedVertices = new ArrayList<>(as.graph.getNumOfVertices() - 1);
        this.tour = new ArrayList<>(as.graph.getNumOfVertices() + 1);
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
    }

    /**
     * Resets the ant to the starting values.
     */
    public void reset() {
        this.currentVertex = as.getAntPosition(id);
        this.tourLength = 0.0;
        this.tour.clear();
        this.notVisitedVertices = as.initializeNotVisitedVertices(currentVertex);
        this.tour.add(currentVertex);
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
    }

    /**
     * Construct the tour of the ant.
     */
    public void explore() {
        // exploration ends when their is no more vertex to visit
        while (!notVisitedVertices.isEmpty()) {
            // get the next vertex
            int nextVertex = selectNextVertex();

            // remove the next vertex from the list of not visited vertices
            notVisitedVertices.remove((Integer) nextVertex);

            // add the next vertex to the tour
            tour.add(nextVertex);

            // mark the used edges
            path[currentVertex][nextVertex] = 1;
            path[nextVertex][currentVertex] = 1;

            // next vertex is the new current vertex
            currentVertex = nextVertex;
        }
        // complete the circular tour
        tour.add(tour.get(0));

        // compute the length of the tour
        computeTourLength();
    }

    /**
     * Computes the length of the tour.
     */
    private void computeTourLength() {
        for (int i = 0; i < tour.size() - 1; i++) {
            tourLength += as.graph.getDistance(tour.get(i), tour.get(i + 1));
        }
    }

    /**
     * Returns an integer which represents the next selected vertex from the not visited vertices list.
     *
     * @return The next selected vertex.
     */
    private int selectNextVertex() {
        double[] tij = new double[as.graph.getNumOfVertices()];     // pheromone intensity
        double[] nij = new double[as.graph.getNumOfVertices()];     // visibility

        double sum = 0.0;   // sum of tij * nij
        // update the sum
        for (int j : notVisitedVertices) {
            tij[j] = Math.pow(as.graph.getTau(currentVertex, j), as.getAlpha());
            nij[j] = Math.pow(as.graph.getTau(currentVertex, j), as.getBeta());
            sum += tij[j] * nij[j];
        }

        // compute the probabilities
        double[] probability = new double[as.graph.getNumOfVertices()];
        double sumProbability = 0.0;
        for (int j : notVisitedVertices) {
            probability[j] = (tij[j] * nij[j]) / sum;
            sumProbability += probability[j];
        }

        // select the next vertex by probability
        return rouletteWheelSelection(probability, sumProbability);
    }

    /**
     * Returns an integer which represents the chosen vertex.
     *
     * @param probability    A double array with the probability of each vertex.
     * @param sumProbability The sum of the probabilities.
     * @return The selected vertex.
     */
    private int rouletteWheelSelection(double[] probability, double sumProbability) {
        double randNum = new Random().nextDouble() * sumProbability;    // random number in range 0...sumProbability
        int selection = 0;
        double accumulatedProbability = probability[selection];
        while (accumulatedProbability < randNum) {
            selection++;
            accumulatedProbability += probability[selection];
        }
        return selection;
    }

    /**
     * Convert the ant tour to an integer array.
     *
     * @return The tour as an int[].
     */
    public int[] getTour() {
        return tour.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Construct a tour for the ant.
     */
    @Override
    public void run() {
        reset();
        explore();
    }
}