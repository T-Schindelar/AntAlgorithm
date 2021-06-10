package AntColonyOptimization.Ant;

import AntColonyOptimization.AntColonyOptimization;

import java.util.ArrayList;
import java.util.List;

/**
 * Ant for the Ant System.
 */
public class Ant implements Runnable {
    /**
     * The Ant Colony Optimization algorithm.
     */
    private final AntColonyOptimization aco;
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
     * The length of the tour.
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
     * @param aco       The instance of the Ant System in which the ant lives.
     * @param id       The id for the ant.
     * @param capacity The capacity of the ant.
     */
    public Ant(AntColonyOptimization aco, int id, int capacity) {
        this.id = id;
        this.aco = aco;
        this.notVisitedVertices = new ArrayList<>(aco.getNumOfVertices() - 1);
        this.feasibleVertices = new ArrayList<>(aco.getNumOfVertices() - 1);
        this.tour = new ArrayList<>(aco.getNumOfVertices() + 1);
        this.path = new int[aco.getNumOfVertices()][aco.getNumOfVertices()];
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
        this.currentVertex = aco.getAntPosition(id);
        this.tourLength = 0.0;
        this.tour.clear();
        this.numOfRoutes = 0;
        this.notVisitedVertices = aco.initializeNotVisitedVertices(currentVertex);
        this.path = new int[aco.getNumOfVertices()][aco.getNumOfVertices()];
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
                int nextVertex = aco.getAntExplorationRule().selectNextVertex(this);

                // remove the next vertex from the list of not visited vertices
                notVisitedVertices.remove((Integer) nextVertex);
                feasibleVertices.remove((Integer) nextVertex);

                // add the next vertex to the tour
                tour.add(nextVertex);

                // increase load
                currentLoad += aco.getDemands(nextVertex);

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
     * Update the feasible vertices for the next selection.
     * A vertex is feasible if the additional demand does not exceed the capacity otherwise it will be removed.
     */
    private void updateFeasibleVertices() {
        feasibleVertices.removeIf(feasibleVertex -> currentLoad + aco.getDemands(feasibleVertex) > capacity);
    }

    /**
     * Computes the length of the tour.
     */
    private void computeTourLength() {
        for (int i = 0; i < tour.size() - 1; i++) {
            tourLength += aco.getDistance(tour.get(i), tour.get(i + 1));
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

    /**
     * Gets the ant system.
     *
     * @return The ant system.
     */
    public AntColonyOptimization getAco() {
        return aco;
    }

    /**
     * Gets the not visited vertices.
     *
     * @return The not visited vertices.
     */
    public List<Integer> getNotVisitedVertices() {
        return notVisitedVertices;
    }

    /**
     * Gets the feasible vertices.
     *
     * @return The feasible vertices.
     */
    public List<Integer> getFeasibleVertices() {
        return feasibleVertices;
    }

    /**
     * Gets the current vertex of teh ant.
     *
     * @return The current vertex.
     */
    public int getCurrentVertex() {
        return currentVertex;
    }
}