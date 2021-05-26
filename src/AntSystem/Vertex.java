package AntSystem;

/** Vertex for a graph. */
public record Vertex(int x, int y) {
    /**
     * Returns the distance (rounded to two decimal places) from this vertex to another vertex.
     * Using the euclidean distance to compute the distance.
     *
     * @param other other vertex
     * @return the distance (rounded to two decimal places) from this vertex to another vertex
     */
    public double distanceTo(Vertex other) {
        return Math.round(Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.x - other.y, 2)) * 100) / 100.0;
    }
}
