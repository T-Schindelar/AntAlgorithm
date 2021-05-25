package AntSystem;

public class Graph {
    private final int numOfVertices;
    protected final Vertex[] vertices;
    protected final double[][] distanceMatrix;
    private final double[][] pheromoneMatrix;
    private final double initialTau;

    public Graph(Vertex[] vertices) {
        this.vertices = vertices;
        this.numOfVertices = vertices.length;
        this.distanceMatrix = new double[numOfVertices][numOfVertices];
        this.pheromoneMatrix = new double[numOfVertices][numOfVertices];
        this.initialTau = 0.1;
        initializePheromoneMatrix();
        initializeDistanceMatrix();
    }

    public Graph(Vertex[] vertices, double initialTau) {
        this.vertices = vertices;
        this.numOfVertices = vertices.length;
        this.distanceMatrix = new double[numOfVertices][numOfVertices];
        this.pheromoneMatrix = new double[numOfVertices][numOfVertices];
        this.initialTau = initialTau;
        initializePheromoneMatrix();
        initializeDistanceMatrix();
    }

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

    private void initializePheromoneMatrix() {
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = i + 1; j < numOfVertices; j++) {
                pheromoneMatrix[i][j] = initialTau;
                pheromoneMatrix[j][i] = initialTau;
            }
        }
    }

    public double getDistance(int i, int j) {
        return distanceMatrix[i][j];
    }

    public double getTau(int i, int j) {
        return pheromoneMatrix[i][j];
    }

    public void setTau(int i, int j, double value) {
        this.pheromoneMatrix[i][j] = value;
    }

    public int getNumOfVertices() {
        return numOfVertices;
    }
}
