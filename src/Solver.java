import Runner.RunACS;
import Runner.RunAS;
import Runner.RunNNH;

import java.util.HashMap;

/**
 * The main application to solve the problems.
 * Solving with all procedures.
 */
public class Solver {
    public static void main(String[] args) {
        // general settings
        int numOfSolutions = 10;
        String directoryOfTheFiles = "src/Files/";
        String dataSet = "Vrp_Set_A";

        // parameter
        double alpha, beta, rho, tau0, q0;
        int numIt;

        // parameter setting to run
        HashMap<String, double[]> parameterDict = new HashMap<>();
        parameterDict.put("alpha", new double[]{1d, 0d, 2d, 5d});
        parameterDict.put("beta", new double[]{1d, 0d, 2d, 5d});
        parameterDict.put("rho", new double[]{0.1, 0.3, 0.6, 0, 9});
        parameterDict.put("tau0", new double[]{0.1, 0.01, 1d, 5d});
        parameterDict.put("q0", new double[]{0.5, 0.25, 0.75});
        parameterDict.put("numIt", new double[]{100d, 10d, 50d, 250d, 500d, 1000d, 2000d});

        for (String parameter : parameterDict.keySet()) {
            alpha = parameterDict.get("alpha")[0];
            beta = parameterDict.get("beta")[0];
            rho = parameterDict.get("rho")[0];
            tau0 = parameterDict.get("tau0")[0];
            q0 = parameterDict.get("q0")[0];
            numIt = (int) parameterDict.get("numIt")[0];

            // runs the ant system & the ant colony system with different settings
            switch (parameter) {
                case "alpha":
                    for (var val : parameterDict.get(parameter))
                        new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, val, beta, rho, tau0, numIt)
                                .solveAllFiles();
                    break;
                case "q0":
                    for (double val : parameterDict.get(parameter))
                        new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, rho, tau0, numIt, val)
                                .solveAllFiles();
                    break;
                case "beta":
                    for (var val : parameterDict.get(parameter)) {
                        new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, val, rho, tau0, numIt)
                                .solveAllFiles();
                        new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, val, rho, tau0, numIt, q0)
                                .solveAllFiles();
                    }
                    break;
                case "rho":
                    for (double val : parameterDict.get(parameter)) {
                        new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, beta, val, tau0, numIt)
                                .solveAllFiles();
                        new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, val, tau0, numIt, q0)
                                .solveAllFiles();
                    }
                    break;
                case "tau0":
                    for (double val : parameterDict.get(parameter)) {
                        new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, beta, rho, val, numIt)
                                .solveAllFiles();
                        new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, rho, val, numIt, q0)
                                .solveAllFiles();
                    }
                    break;
                case "numIt":
                    for (double val : parameterDict.get(parameter)) {
                        new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, beta, rho, tau0, (int) val)
                                .solveAllFiles();
                        new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, rho, tau0, (int) val, q0)
                                .solveAllFiles();
                    }
                    break;
                default:
                    break;
            }
        }
        // runs the nearest neighbour heuristic
        new RunNNH(directoryOfTheFiles, dataSet).solveAllFiles();
    }

    /**
     * Solves the problem instances from zero to the specific index.
     * Solving with all procedures.
     *
     * @param toIndex             The specific index.
     * @param numOfSolutions      The Number of Solutions for each instance.
     * @param directoryOfTheFiles The directory of the files.
     * @param dataSet             The dataset to solve.
     * @param alpha               The alpha value.
     * @param beta                The beta value.
     * @param rho                 The rho value.
     * @param tau0                The initial tau value.
     * @param q0                  The q0 value.
     * @param numIt               The number of iterations value.
     */
    private static void solveToIndex(int toIndex, int numOfSolutions, String directoryOfTheFiles, String dataSet, double alpha,
                                     double beta, double rho, double tau0, double q0, int numIt) {
        // runs the ant system
        RunAS runAS = new RunAS(directoryOfTheFiles, dataSet, numOfSolutions, alpha, beta, rho, tau0, numIt);
        for (int i = 0; i <= toIndex; i++)
            runAS.solveOneFile(i);

        // runs the ant colony system
        RunACS runACS = new RunACS(directoryOfTheFiles, dataSet, numOfSolutions, beta, rho, tau0, numIt, q0);
        for (int i = 0; i <= toIndex; i++)
            runACS.solveOneFile(i);

        // runs the nearest neighbour heuristic
        RunNNH runNNH = new RunNNH(directoryOfTheFiles, dataSet);
        for (int i = 0; i <= toIndex; i++)
            runNNH.solveOneFile(i);
    }
}