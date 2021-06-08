import AntSystem.AS;

import java.util.Arrays;

/**
 * Test the Ant System.
 */
public class TestAS {
    public static void main(String[] args) {
        // initialize the Ant System
        AS as = new AS(AS.createRandomVertices(100, 100), 100);
        // printMatrix(as.graph.distanceMatrix);

        // set parameter
        as.setNumberOfIterations(1000);
        as.setAlpha(1.0);
        as.setBeta(1.0);
        as.setRho(0.01);
        as.setInitialTau(0.01);

        // solve and measure time
        long start = System.currentTimeMillis();
        as.solve();
        long finish = System.currentTimeMillis();
        long compTime = finish - start;

        // print result
        System.out.println(Arrays.toString(as.getBestTour()));
        System.out.println(as.getBestTourLength());
        System.out.printf("compTime: %f s", compTime / 1000.0);
    }
}