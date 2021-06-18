import Runner.RunACS;
import Runner.RunAS;
import Runner.RunNNH;

// TODO: 18.06.21 add documentation

/**
 * The main application to solve the problems.
 */
public class Solver {
    public static void main(String[] args) {
        int toIndex = 0;
        int numOfSolutions = 10;
        String directoryOfTheFiles = "/home/tobias/Programmierung/Java/AntAlgorithm/src/Files/";

        // parameter
        double alpha = 1;
        double beta = 1;
        double rho = 0.1;
        double tau0 = 0.1;
        double q0 = 0.5;
        double phi = 0.2;
        int numIt = 1000;

        // runs the Ant System
        RunAS runAS = new RunAS(directoryOfTheFiles, numOfSolutions, alpha, beta, rho, tau0, numIt);
        // runAS.solveAllFiles();
        for (int i = 0; i <= toIndex; i++)
            runAS.solveOneFile(i);

        // runs the Ant Colony System
        RunACS runACS = new RunACS(directoryOfTheFiles, numOfSolutions, alpha, beta, rho, tau0, numIt, q0, phi);
        // runACS.solveAllFiles();
        for (int i = 0; i <= toIndex; i++)
            runACS.solveOneFile(i);

        // runs the nearest neighbour heuristic
        RunNNH runNNH = new RunNNH(directoryOfTheFiles);
        runNNH.solveAllFiles();
        // runNNH.solveOneFile(index);
    }
}