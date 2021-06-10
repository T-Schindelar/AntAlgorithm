package AntSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ant for the Ant System.
 */
public class Ant implements Runnable {
    /**
     * The Ant System (AS) in which the ant lives.
     */
    private final AS as;
    /**
     * Identifier for the ant.
     */
    private final int id;
    /**
     * The capacity of the ant.
     */
    private final int capacity;
    /**
     * A List of feasible vertices for the next selection.
     */
    private final List<Integer> feasibleVertices;
    /**
     * The created Tour of the ant.
     */
    private final List<Integer> tour;
    /**
     * Current vertex.
     */
    private int currentVertex;
    /**
     * The current load of the ant.
     */
    private int currentLoad;
    /**
     * The List of not visited vertices.
     */
    private List<Integer> notVisitedVertices;
    /**
     * The lenght of the tour.
     */
    private double tourLength;
    /**
     * The traveled path of the ant.
     */
    private int[][] path;
    /**
     * The number of used routes.
     */
    private int numOfRoutes;


    /**
     * Constructor.
     *
     * @param as       The instance of the Ant System in which the ant lives.
     * @param id       The id for the ant.
     * @param capacity The capacity of the ant.
     */
    public Ant(AS as, int id, int capacity) {
        this.id = id;
        this.as = as;
        this.notVisitedVertices = new ArrayList<>(as.graph.getNumOfVertices() - 1);
        this.feasibleVertices = new ArrayList<>(as.graph.getNumOfVertices() - 1);
        this.tour = new ArrayList<>(as.graph.getNumOfVertices() + 1);
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
        this.capacity = capacity;
    }

    /**
     * Construct a tour for the ant.
     */
    @Override
    public void run() {
        reset();
        explore();
    }

    /**
     * Resets the ant to the starting values.
     */
    public void reset() {
        this.currentVertex = as.getAntPosition(id);
        this.tourLength = 0.0;
        this.tour.clear();
        this.numOfRoutes = 0;
        this.notVisitedVertices = as.initializeNotVisitedVertices(currentVertex);
        this.path = new int[as.graph.getNumOfVertices()][as.graph.getNumOfVertices()];
    }

    /**
     * Construct the tour of the ant.
     */
    public void explore() {
        // exploration ends when their is no more vertex to visit
        while (!notVisitedVertices.isEmpty()) {
            currentLoad = 0;
            tour.add(currentVertex);
            feasibleVertices.addAll(notVisitedVertices);
            while (!feasibleVertices.isEmpty()) {
                // get the next vertex
                int nextVertex = selectNextVertex();

                // remove the next vertex from the list of not visited vertices
                notVisitedVertices.remove((Integer) nextVertex);
                feasibleVertices.remove((Integer) nextVertex);

                // add the next vertex to the tour
                tour.add(nextVertex);

                // increase load
                currentLoad += as.graph.demands[nextVertex];

                // mark the used edges
                path[currentVertex][nextVertex] = 1;
                path[nextVertex][currentVertex] = 1;

                // next vertex is the new current vertex
                currentVertex = nextVertex;

                updateFeasibleVertices();
            }
            // complete the route
            tour.add(tour.get(0));
            numOfRoutes++;

            // mark the used edges
            path[currentVertex][tour.get(0)] = 1;
            path[tour.get(0)][currentVertex] = 1;

            // set starting point of the next route
            currentVertex = tour.get(0);
        }
        // compute the length of the tour
        computeTourLength();
    }

    /**
     * Returns an integer which represents the next selected vertex from the not visited vertices list.
     * Considering the demands and the capacity.
     *
     * @return The next selected vertex.
     */
    private int selectNextVertex() {
        double[] tij = new double[as.graph.getNumOfVertices()];     // pheromone intensity
        double[] nij = new double[as.graph.getNumOfVertices()];     // visibility

        double sum = 0.0;   // sum of tij * nij
        // update the sum
        for (int j : feasibleVertices) {
            // calculate the intensity of the trail
            tij[j] = Math.pow(as.graph.getTau(currentVertex, j), as.getAlpha());

            // calculate the visibility of the trail, quantity = 1 / d_ij
            nij[j] = Math.pow(1 / as.graph.getDistance(currentVertex, j), as.getBeta());

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
     * Update the feasible vertices for the next selection.
     * A vertex is feasible if the additional demand does not exceed the capacity otherwise it will be removed.
     */
    private void updateFeasibleVertices() {
        feasibleVertices.removeIf(feasibleVertex -> currentLoad + as.graph.demands[feasibleVertex] > capacity);
    }

    /**
     * Returns an integer which represents the selected vertex.
     * Selects randomly a vertex with probability p_ij.
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
     * Computes the length of the tour.
     */
    private void computeTourLength() {
        for (int i = 0; i < tour.size() - 1; i++) {
            tourLength += as.graph.getDistance(tour.get(i), tour.get(i + 1));
        }
    }

    /**
     * Gets the value of path at position i, j.
     *
     * @param i Row i.
     * @param j Column j
     * @return The value of path at position i, j.
     */
    public int getPathValue(int i, int j) {
        return path[i][j];
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
     * Gets the length of the tour.
     *
     * @return The tour length.
     */
    public double getTourLength() {
        return tourLength;
    }
}