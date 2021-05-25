package AntSystem;

public class Vertex {
    private final int x;
    private final int y;
    private final boolean visited;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        this.visited = false;
    }

    // computing euclidean distance, rounded to two decimal places
    public double distanceTo(Vertex other) {
//        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.x - other.y, 2));
        return Math.round(Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.x - other.y, 2)) * 100) / 100.0;
    }

    public boolean isVisited() {
        return visited;
    }

    // todo löschen
    @Override
    public String toString() {
        return "AS.Vertex{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
