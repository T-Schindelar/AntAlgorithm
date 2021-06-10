import AntColonyOptimization.AntColonySystem;
import AntColonyOptimization.Utilities.FileLoader;
import AntColonyOptimization.Utilities.ProblemInstance;

import java.io.File;
import java.util.Arrays;

/**
 * Test the Ant Colony Optimization.
 */
public class RunACS {
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
                AntColonySystem acs = new AntColonyOptimization.AntColonySystem(problem);

                // set parameter
                acs.setNumberOfIterations(100);
                acs.setAlpha(1);
                acs.setBeta(1);
                acs.setRho(0.01);
                acs.setInitialTau(0.01);
                acs.setQ0(0.5);

                // solve and measure time
                System.out.println(problem.getName());
                System.out.println("Solving, please wait...");
                long start = System.currentTimeMillis();
                acs.solve();
                long finish = System.currentTimeMillis();
                long compTime = finish - start;

                // print result
                System.out.println(Arrays.toString(acs.getBestTour()));
                System.out.println(acs.getBestTourLength());
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
        AntColonySystem acs = new AntColonySystem(problem);

        // set parameter
        acs.setNumberOfIterations(100);
        acs.setAlpha(1);
        acs.setBeta(1);
        acs.setRho(0.01);
        acs.setInitialTau(0.01);
        acs.setQ0(0.5);

        // solve and measure time
        System.out.println(problem.getName());
        System.out.println("Solving, please wait...");
        long start = System.currentTimeMillis();
        acs.solve();
        long finish = System.currentTimeMillis();
        long compTime = finish - start;

        // print result
        System.out.println(Arrays.toString(acs.getBestTour()));
        System.out.println(acs.getBestTourLength());
        System.out.printf("compTime: %f s \n", compTime / 1000.0);
    }
}