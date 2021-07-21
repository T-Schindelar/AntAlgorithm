import Runner.RunACS;
import Runner.RunAS;

import java.util.HashMap;

/**
 * The main application to solve the problems.
 */
public class Solver {
    public static void main(String[] args) {
        int toIndex = 5;
        int numOfSolutions = 10;
        String directoryOfTheFiles = "src/Files/";
        String dataSet = "Vrp_Set_A";

        // parameter
        double alpha, beta, rho, tau0, q0;
        int numIt;

        HashMap<String, double[]> parameterDict = new HashMap<>();
        parameterDict.put("alpha", new double[]{1d, 2d, 5d});
        parameterDict.put("beta", new double[]{1d, 2d, 5d});
        parameterDict.put("rho", new double[]{0.1, 0.3, 0.6});
        parameterDict.put("tau0", new double[]{0.01, 0.1, 1d});
        parameterDict.put("q0", new double[]{0.25, 0.5, 0.75});
        parameterDict.put("numIt", new double[]{10d, 100d, 500d, 1000d});

        for (String parameter : parameterDict.keySet()) {
            alpha = parameterDict.get("alpha")[0];
            beta = parameterDict.get("beta")[0];
            rho = parameterDict.get("rho")[0];
            tau0 = parameterDict.get("tau0")[0];
            q0 = parameterDict.get("q0")[0];
            numIt = (int) parameterDict.get("numIt")[0];

            switch (parameter){
                case "alpha":
                    for(var val : parameterDict.get(parameter))
                        solve(toIndex, numOfSolutions, directoryOfTheFiles, dataSet, val, beta, rho, tau0, q0, numIt);
                    break;
                case "beta":
                    for(var val : parameterDict.get(parameter))
                        solve(toIndex, numOfSolutions, directoryOfTheFiles, dataSet, alpha, val, rho, tau0, q0, numIt);
                    break;
                case "rho":
                    for(double val : parameterDict.get(parameter))
                        solve(toIndex, numOfSolutions, directoryOfTheFiles, dataSet, alpha, beta, val, tau0, q0, numIt);
                    break;
                case "tau0":
                    for(double val : parameterDict.get(parameter))
                        solve(toIndex, numOfSolutions, directoryOfTheFiles, dataSet, alpha, beta, rho, val, q0, numIt);
                    break;
                case "q0":
                    for(double val : parameterDict.get(parameter))
                        solve(toIndex, numOfSolutions, directoryOfTheFiles, dataSet, alpha, beta, rho, tau0, val, numIt);
                    break;
            }
        }
    }

    private static void solve(int toIndex, int numOfSolutions, String directoryOfTheFiles, String dataSet, double alpha,
                              double beta, double rho, double tau0, double q0, int numIt) {
        // runs the Ant System
        RunAS runAS = new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, beta, rho, tau0, numIt);
        runAS.solveAllFiles();
        for (int i = 0; i <= toIndex; i++)
            runAS.solveOneFile(i);

        // runs the Ant Colony System
        RunACS runACS = new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, rho, tau0, numIt, q0);
        runACS.solveAllFiles();
        for (int i = 0; i <= toIndex; i++)
            runACS.solveOneFile(i);

        // runs the nearest neighbour heuristic
//        RunNNH runNNH = new RunNNH(directoryOfTheFiles, dataSet);
//        runNNH.solveAllFiles();
//        for (int i = 0; i <= toIndex; i++)
//            runNNH.solveOneFile(i);
    }
}