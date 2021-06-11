package NearestNeighborHeuristic;

import Graph.Graph;
import Utilities.ProblemInstance;

import java.util.ArrayList;

/**
 * Nearest Neighbor Heuristic to solve CVRPs.
 */
public class NearestNeighborHeuristic {
    /**
     * The graph for the problem.
     */
    private final Graph graph;
    /**
     * The capacity of a vehicle.
     */
    private final int capacity;
    /**
     * The created Tour.
     */
    private final ArrayList<Integer> tour;
    /**
     * The current load of the vehicle.
     */
    private int currentLoad;
    /**
     * The length of the tour.
     */
    private double tourLength;

    /**
     * The feasible vertices for the next selection.
     */
    private final ArrayList<Integer> feasibleVertices;


    /**
     * Constructor.
     * Construct the graph for the problem.
     *
     * @param problem The problem to solve.
     */
    public NearestNeighborHeuristic(ProblemInstance problem) {
        this.graph = new Graph(problem.getVertices(), problem.getDemands());
        this.tour = new ArrayList<>(graph.getNumOfVertices() + 1);
        this.capacity = problem.getVehicleCapacity();
        feasibleVertices = new ArrayList<>(graph.getNumOfVertices());
    }

    /**
     * Solve the Problem.
     */
    public void solve() {
        // start tour at depot
        int currentVertex = 0;

        // initialize not visited vertices
        ArrayList<Integer> notVisitedVertices = new ArrayList<>(graph.getNumOfVertices());
        for (int i = 1; i < graph.getNumOfVertices(); i++)
            notVisitedVertices.add(i);

        // ends when their is no more vertex to visit
        while (!notVisitedVertices.isEmpty()) {
            currentLoad = 0;
            tour.add(currentVertex);
            feasibleVertices.addAll(notVisitedVertices);
            while (!feasibleVertices.isEmpty()) {
                // get the next vertex
                int nextVertex = getNextVertex(currentVertex);

                // remove the next vertex from the list of not visited vertices
                notVisitedVertices.remove((Integer) nextVertex);
                feasibleVertices.remove((Integer) nextVertex);

                // add the next vertex to the tour
                tour.add(nextVertex);

                // increase load
                currentLoad += graph.getDemands(nextVertex);

                // next vertex is the new current vertex
                currentVertex = nextVertex;

                updateFeasibleVertices();
            }
            // complete the route
            tour.add(tour.get(0));

            // set starting point of the next route
            currentVertex = tour.get(0);
        }
        // compute the length of the tour
        computeTourLength();
    }

    /**
     * Gets the next vertex.
     * Selects the shortest distance between the current vertex to another vertex.
     *
     * @param currentVertex The current vertex.
     * @return The next vertex to visit.
     */
    private int getNextVertex(int currentVertex) {
        int nextVertex = feasibleVertices.get(0);
        double min = graph.getDistance(currentVertex, nextVertex);

        for (int j : feasibleVertices) {
            if (graph.getDistance(currentVertex, j) < min) {
                nextVertex = j;
                min = graph.getDistance(currentVertex, nextVertex);
            }
        }
        return nextVertex;
    }

    /**
     * Update the feasible vertices for the next selection.
     * A vertex is feasible if the additional demand does not exceed the capacity otherwise it will be removed.
     */
    private void updateFeasibleVertices() {
        feasibleVertices.removeIf(feasibleVertex -> currentLoad + graph.getDemands(feasibleVertex) > capacity);
    }

    /**
     * Computes the length of the tour.
     */
    private void computeTourLength() {
        for (int i = 0; i < tour.size() - 1; i++) {
            tourLength += graph.getDistance(tour.get(i), tour.get(i + 1));
        }
    }

    /**
     * Initializes the not visited vertices.
     *
     * @return A list of not visited vertices for the ant.
     */
    public ArrayList<Integer> initializeNotVisitedVertices() {
        ArrayList<Integer> notVisitedVertices = new ArrayList<>();
        for (int i = 1; i < graph.getNumOfVertices(); i++) notVisitedVertices.add(i);
        return notVisitedVertices;
    }

    /**
     * Gets the tour.
     *
     * @return The tour.
     */
    public int[] getTour() {
        return tour.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Gets the length of the tour.
     *
     * @return The length of the tour.
     */
    public double getTourLength() {
        return tourLength;
    }
}