package AntColonyOptimization.Graph;

/**
 * Vertex for a graph.
 */
public record Vertex(int x, int y) {
    /**
     * Returns the distance (rounded to two decimal places) from this vertex the other vertex.
     * Using the euclidean distance to compute the distance.
     *
     * @param other The other vertex.
     * @return The distance (rounded to two decimal places) from this vertex the other vertex.
     */
    public double distanceTo(Vertex other) {
        return Math.round(Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2)));
    }
}