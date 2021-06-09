package AntSystem;

import java.util.Arrays;

/**
 * Test the Ant System.
 */
public class TestAS {
    public static void main(String[] args) {
//        // test loader
//        FileLoader fl = new FileLoader("/home/tobias/Programmierung/Java/AntAlgorithm/src/" +
//                "Problem_Instances/Vrp_Set_A");
//        if (fl.loadFiles(".vrp"))
//            for (File f : fl) {
//                System.out.println(f.getName());
//                ProblemInstanceLoader problem = new ProblemInstanceLoader(f);
//                problem.loadInstance();
//                System.out.println(problem.getNumOfVehicle() + " " + problem.getCapacity() + " "
//                        + problem.getNumOfVertices());
//            }

        // test loader
        FileLoader fl = new FileLoader("/home/tobias/Programmierung/Java/AntAlgorithm/src/" +
                "Problem_Instances/Vrp_Set_A");
        fl.loadFiles(".vrp");
        ProblemInstanceLoader problem = new ProblemInstanceLoader(fl.getFile(0));
        problem.loadInstance();
        System.out.println(problem.getName() + problem.getNumOfVehicle() + " " + problem.getCapacity() + " "
                + problem.getNumOfVertices());

        // initialize the Ant System
        AS as = new AS(problem.getVertices(), problem.getNumOfVertices());

        // set parameter
        as.setNumberOfIterations(100);
        as.setAlpha(1.0);
        as.setBeta(5);
        as.setRho(0.01);
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

//        Route #1: 21 31 19 17 13 7 26
//        Route #2: 12 1 16 30
//        Route #3: 27 24
//        Route #4: 29 18 8 9 22 15 10 25 5 20
//        Route #5: 14 28 11 4 23 3 2 6
        int[] tour = {0, 21, 31, 19, 17, 13, 7, 26, 0, 0, 12, 1, 16, 30, 0, 0, 27, 24, 0, 0, 29, 18, 8,
                9, 22, 15, 10, 25, 5, 20, 0, 0, 14, 28, 11, 4, 23, 3, 2, 6, 0};
        double tourLength = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            System.out.print(tour[i] + ", ");
            tourLength += as.graph.getDistance(tour[i], tour[i + 1]);
        }
        System.out.println();
        System.out.println(tourLength);

    }
}