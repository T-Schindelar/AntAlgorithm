package Graph;

/**
 * Graph represents the environment.
 */
public class Graph {
    /**
     * Vertices of the graph.
     */
    private final Vertex[] vertices;
    /**
     * Demands from the vertices.
     */
    private final int[] demands;
    /**
     * Distance matrix of the graph.
     */
    private final double[][] distanceMatrix;
    /**
     * Pheromone matrix of the graph.
     */
    private final double[][] pheromoneMatrix;
    /**
     * Number of vertices.
     */
    private final int numOfVertices;
    /**
     * The initial tau value for the pheromone matrix.
     */
    private double initialTau = 0.1;

    /**
     * Constructor.
     *
     * @param vertices Vertices of the graph.
     * @param demands  Demands from the vertices.
     */
    public Graph(Vertex[] vertices, int[] demands) {
        this.vertices = vertices;
        this.demands = demands;
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
     * Gets the distance for the edge(i,j).
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return Distance for the edge(i,j).
     */
    public double getDistance(int i, int j) {
        return distanceMatrix[i][j];
    }

    /**
     * Gets the pheromone value of the edge(i,j).
     *
     * @param i The current vertex.
     * @param j The next vertex.
     * @return Pheromone value of the edge(i,j).
     */
    public double getTau(int i, int j) {
        return pheromoneMatrix[i][j];
    }

    /**
     * Sets the pheromone value for the edge(i,j).
     *
     * @param i     The current vertex.
     * @param j     The next vertex.
     * @param value The new pheromone value for the edge(i,j).
     */
    public void setTau(int i, int j, double value) {
        this.pheromoneMatrix[i][j] = value;
    }

    /**
     * Gets the number of vertices of the graph.
     *
     * @return Number of vertices of the graph.
     */
    public int getNumOfVertices() {
        return numOfVertices;
    }

    /**
     * Gets the value of the initial tau.
     *
     * @return The value of the initial tau.
     */
    public double getInitialTau() {
        return initialTau;
    }

    /**
     * Sets the tau value and initializes the pheromone matrix with this value
     *
     * @param initialTau The new initial tau value.
     */
    public void setInitialTau(double initialTau) {
        this.initialTau = initialTau;
        initializePheromoneMatrix();
    }

    /**
     * Gets the demand of a vertex.
     *
     * @return Demand of vertex i.
     */
    public int getDemands(int i) {
        return demands[i];
    }
}