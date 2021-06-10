import AntSystem.AS;
import AntSystem.FileLoader;
import AntSystem.ProblemInstance;

import java.io.File;
import java.util.Arrays;

/**
 * Test the Ant Colony Optimization.
 */
public class TestAS {
    public static void main(String[] args) {
//        testAllFiles();
        testFile(0);
    }

    public static void testAllFiles() {
        // test loader
        FileLoader fl = new FileLoader("/home/tobias/Programmierung/Java/AntAlgorithm/src/" +
                "Problem_Instances/Vrp_Set_A");
        if (fl.loadFiles(".vrp"))
            for (File f : fl) {
                ProblemInstance problem = new ProblemInstance(f);
                problem.loadInstance();

                // initialize the Ant System
                AS as = new AS(problem);

                // set parameter
                as.setNumberOfIterations(1);
                as.setAlpha(1);
                as.setBeta(1);
                as.setRho(0.1);
                as.setInitialTau(0.01);

                // solve and measure time
                System.out.println(problem.getName());
                System.out.println("Solving, please wait...");
                long start = System.currentTimeMillis();
                as.solve();
                long finish = System.currentTimeMillis();
                long compTime = finish - start;

                // print result
                System.out.println(Arrays.toString(as.getBestTour()));
                System.out.println(as.getBestTourLength());
                System.out.printf("compTime: %f s \n", compTime / 1000.0);
            }
    }

    public static void testFile(int index) {
        // load files
        FileLoader fl = new FileLoader("/home/tobias/Programmierung/Java/AntAlgorithm/src/" +
                "Problem_Instances/Vrp_Set_A");
        fl.loadFiles(".vrp");

        // load problems
        ProblemInstance problem = new ProblemInstance(fl.getFile(index));
        problem.loadInstance();

        // initialize the Ant System
        AS as = new AS(problem);

        // set parameter
        as.setNumberOfIterations(1);
        as.setAlpha(1);
        as.setBeta(1);
        as.setRho(0.1);
        as.setInitialTau(0.01);

        // solve and measure time
        System.out.println(problem.getName());
        System.out.println("Solving, please wait...");
        long start = System.currentTimeMillis();
        as.solve();
        long finish = System.currentTimeMillis();
        long compTime = finish - start;

        // print result
        System.out.println(Arrays.toString(as.getBestTour()));
        System.out.println(as.getBestTourLength());
        System.out.printf("compTime: %f s \n", compTime / 1000.0);
    }
}