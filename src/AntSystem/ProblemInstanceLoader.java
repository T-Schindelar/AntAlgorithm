package AntSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The ProblemInstanceLoader class loads a problem instance from a specific file,
 * to get the vertices and their demands.
 */
public class ProblemInstanceLoader {
    /**
     * The specific file.
     */
    private final File file;

    /**
     * The vertices of the file.
     */
    private final ArrayList<Vertex> vertices = new ArrayList<>();

    /**
     * The demands for the vertices.
     */
    private final ArrayList<Integer> demands = new ArrayList<>();

    /**
     * The problem instance name.
     */
    private String name;

    /**
     * The number of vehicles.
     */
    private int numOfVehicle;

    /**
     * The capacity of a vehicle.
     */
    private int vehicleCapacity;

    /**
     * The number of vertices.
     */
    private int numOfVertices;

    /**
     * Constructor.
     *
     * @param file The specific file.
     */
    public ProblemInstanceLoader(File file) {
        this.file = file;
    }

    /**
     * Loads the problem instance.
     *
     * @return <CODE>true</CODE> if the load was successful, <CODE>false</CODE> otherwise
     */
    public boolean loadInstance() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String delimiter = "\s";
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split(delimiter);
                if (values[0].equals("NAME")) {
                    name = values[2];
                    setNumOfVehicles(values[2]);
                    continue;
                }
                if (values[0].equals("DIMENSION")) {
                    numOfVertices = Integer.parseInt(values[2]);
                    continue;
                }
                if (values[0].equals("CAPACITY")) {
                    vehicleCapacity = Integer.parseInt(values[2]);
                    continue;
                }
                if (values[0].equals("NODE_COORD_SECTION")) {
                    while (vertices.size() < numOfVertices && (line = reader.readLine()) != null) {
                        // filters out the empty values and creates the array
                        String[] coordinates = Arrays.stream(line.split(delimiter))
                                .filter(value -> value != null && value.length() > 0)
                                .toArray(String[]::new);
                        vertices.add(new Vertex(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2])));
                    }
                    continue;
                }
                if (values[0].equals("DEMAND_SECTION")) {
                    while (demands.size() < numOfVertices && (line = reader.readLine()) != null) {
                        String[] demand = line.split(delimiter);
                        demands.add(Integer.parseInt(demand[1]));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets the number of vehicles.
     *
     * @param string The string which contains the number of vehicles.
     */
    private void setNumOfVehicles(String string) {
        String[] splittedString = string.split("k");
        numOfVehicle = Integer.parseInt(splittedString[1]);
    }

    /**
     * Gets the vertices.
     *
     * @return The vertices as an Vertex[].
     */
    public Vertex[] getVertices() {
        return vertices.toArray(Vertex[]::new);
    }

    /**
     * Gets the demands.
     *
     * @return The vertices as an Integer[].
     */
    public Integer[] getDemands() {
        return demands.toArray(Integer[]::new);
    }

    /**
     * Gets the number of vehicles of this problem instance.
     *
     * @return The number of vehicles.
     */
    public int getNumOfVehicle() {
        return numOfVehicle;
    }

    /**
     * Gets the capacity of a vehicle of this problem instance.
     *
     * @return The capacity of a vehicle.
     */
    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    /**
     * Gets the number of vertices of this problem instance.
     *
     * @return The number of vertices.
     */
    public int getNumOfVertices() {
        return numOfVertices;
    }

    /**
     * Gets the problem instance name.
     *
     * @return The problem instance name.
     */
    public String getName() {
        return name;
    }
}