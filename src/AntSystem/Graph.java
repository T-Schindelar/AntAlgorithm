package AntSystem;

/**
 * Graph represents the environment for the Ant System.
 */
public class Graph {
    /**
     * Number of vertices.
     */
    private final int numOfVertices;

    /**
     * Vertices of the graph.
     */
    protected final Vertex[] vertices;

    /**
     * Distance matrix of the graph.
     */
    protected final double[][] distanceMatrix;

    /**
     * Pheromone matrix of the graph.
     */
    private final double[][] pheromoneMatrix;

    /**
     * The initial tau value for the pheromone matrix.
     */
    private double initialTau = 0.1;

    /**
     * Constructor.
     *
     * @param vertices vertices to build the graph
     */
    public Graph(Vertex[] vertices) {
        this.vertices = vertices;
        this.numOfVertices = vertices.length;
        this.distanceMatrix = new double[numOfVertices][numOfVertices];
        this.pheromoneMatrix = new double[numOfVertices][numOfVertices];
        initializePheromoneMatrix();
        initializeDistanceMatrix();
    }

    /**
     * Initialize the distance matrix, using euclidean distances.
     */
    private void initializeDistanceMatrix() {
        double distance;
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = i + 1; j < numOfVertices; j++) {
                distance = vertices[i].distanceTo(vertices[j]);
                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }
    }

    /**
     * Initialize pheromone matrix with the initial tau
     */
    private void initializePheromoneMatrix() {
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = i + 1; j < numOfVertices; j++) {
                pheromoneMatrix[i][j] = initialTau;
                pheromoneMatrix[j][i] = initialTau;
            }
        }
    }

    /**
     * Returns the distance for the edge(i,j).
     *
     * @param i the current vertex
     * @param j the next vertex
     * @return distance for the edge(i,j)
     */
    public double getDistance(int i, int j) {
        return distanceMatrix[i][j];
    }

    /**
     * Returns the pheromone value for the edge(i,j).
     *
     * @param i the current vertex
     * @param j the next vertex
     * @return pheromone value for the edge(i,j).
     */
    public double getTau(int i, int j) {
        return pheromoneMatrix[i][j];
    }

    /**
     * Sets a new pheromone value for the edge(i,j).
     *
     * @param i     the current vertex
     * @param j     the next vertex
     * @param value the new pheromone value for the edge(i,j)
     */
    public void setTau(int i, int j, double value) {
        this.pheromoneMatrix[i][j] = value;
    }

    /**
     * Returns the number of vertices of the graph.
     *
     * @return number of vertices of the graph
     */
    public int getNumOfVertices() {
        return numOfVertices;
    }

    /**
     * Return the value of the initial tau.
     *
     * @return the value of the initial tau
     */
    public double getInitialTau() {
        return initialTau;
    }

    /**
     * Sets the new tau value and initializes the pheromone matrix with this value
     *
     * @param initialTau the new initial tau value
     */
    public void setInitialTau(double initialTau) {
        this.initialTau = initialTau;
        initializePheromoneMatrix();
    }
}
