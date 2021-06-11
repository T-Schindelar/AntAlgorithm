import AntColonyOptimization.AntSystem;
import Utilities.FileLoader;
import Utilities.ProblemInstance;

import java.io.File;
import java.util.Arrays;

/**
 * Runs the Ant System algorithm.
 */
public class RunAS {
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
                AntSystem antSystem = new AntSystem(problem);

                // set parameter
                antSystem.setNumberOfIterations(100);
                antSystem.setAlpha(1);
                antSystem.setBeta(1);
                antSystem.setRho(0.1);
                antSystem.setInitialTau(0.1);

                // solve and measure time
                System.out.println(problem.getName());
                System.out.println("Solving, please wait...");
                long start = System.currentTimeMillis();
                antSystem.solve();
                long finish = System.currentTimeMillis();
                long compTime = finish - start;

                // print result
                System.out.println(Arrays.toString(antSystem.getBestTour()));
                System.out.println(antSystem.getBestTourLength());
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
        AntSystem antSystem = new AntSystem(problem);

        // set parameter
        antSystem.setNumberOfIterations(100);
        antSystem.setAlpha(1);
        antSystem.setBeta(1);
        antSystem.setRho(0.1);
        antSystem.setInitialTau(0.1);

        // solve and measure time
        System.out.println(problem.getName());
        System.out.println("Solving, please wait...");
        long start = System.currentTimeMillis();
        antSystem.solve();
        long finish = System.currentTimeMillis();
        long compTime = finish - start;

        // print result
        System.out.println(Arrays.toString(antSystem.getBestTour()));
        System.out.println(antSystem.getBestTourLength());
        System.out.printf("compTime: %f s \n", compTime / 1000.0);
    }
}