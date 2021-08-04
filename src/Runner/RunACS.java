package Runner;

import AntColonyOptimization.AntColonySystem;
import Utilities.ProblemInstance;
import Utilities.Writer;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * Runs the Ant Colony System algorithm to solve the problem.
 */
public class RunACS extends RunAS {
    /**
     * Probability to select with the deterministic rule.
     */
    private final double q0;

    /**
     * Constructor.
     *
     * @param directory      The directory of the files.
     * @param dataSet        The dataset to solve.
     * @param numOfSolutions Number of solutions to build for one problem.
     * @param beta           The beta value.
     * @param rho            The rho value.
     * @param tau0           The tau0 value.
     * @param numIt          The number of iterations value.
     * @param q0             The Probability to select with the deterministic rule.
     */
    public RunACS(String directory, String dataSet, int numOfSolutions, double beta, double rho, double tau0,
                  int numIt, double q0) {
        super(directory, dataSet, numOfSolutions, 1, beta, rho, tau0, numIt);
        this.q0 = q0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void solveOneFile(File file) {
        // reset saved vales
        reset();

        // initialize problem
        ProblemInstance problem = new ProblemInstance(file);
        problem.loadInstance();

        setOptimalValue(problem.getOptimalValue());

        // initialize writer
        Writer writer = new Writer(directory + "Detailed_Results/ACS/", problem.getName() +
                "_beta" + beta + "_rho" + rho + "_tau0" + tau0 + "_q0" + q0 + "_it" + numIt);
        writer.setHead("problem; " + "beta" + "; " + "rho" + "; " + "tau0" + "; " + "q0" + "; " + "iterations");
        writer.addRecordToBody(problem.getName() + "; " + beta + "; " + rho + "; " + tau0 + "; " + q0 + "; " +
                numIt);
        writer.addRecordToBody("");
        writer.addRecordToBody("nr; solution value; computation time in sec");

        // console output as user information
        System.out.println("Solving " + problem.getName() + ", please wait...");

        // solve the problem numOfSolutions times
        for (int i = 0; i < numOfSolutions; i++) {
            // initialize the Ant Colony System
            AntColonySystem antColonySystem = new AntColonySystem(problem);

            // set parameter
            antColonySystem.setBeta(beta);
            antColonySystem.setRho(rho);
            antColonySystem.setInitialTau(tau0);
            antColonySystem.setNumberOfIterations(numIt);
            antColonySystem.setQ0(q0);

            // solve and measure time
            Instant start = Instant.now();
            antColonySystem.solve();
            Instant finish = Instant.now();
            long compTime = Duration.between(start, finish).toMillis();

            // save results
            solutionValues.add(antColonySystem.getBestTourLength());
            sumSolutionValues += antColonySystem.getBestTourLength();
            sumComputationTime += compTime;

            // set bounds
            setBounds((int) antColonySystem.getBestTourLength());

            // write result
            writer.addRecordToBody(i + 1 + "; " + (int) antColonySystem.getBestTourLength() + "; "
                    + compTime / 1000d);
        }
        writer.addRecordToBody("");
        writer.addRecordToBody("avg; " + getAvgSolutionValue() + "; " + getAvgComputationTime());
        writer.write();

        // write summary
        writer = new Writer(directory + "Summarized_Results/", "summarized_results_acs" +
                "_beta" + beta + "_rho" + rho + "_tau0" + tau0 + "_q0" + q0 + "_it" + numIt);
        boolean append = true;

        // resets the file and sets the heading
        if (!summarizedFileHasHead) {
            writer.setHead("problem; optimal value; avg. results; gap in %; lower bound; upper bound;" +
                    " standard deviation; avg. comp. time;");
            summarizedFileHasHead = true;
            append = false;
        }

        writer.addRecordToBody(problem.getName() + "; " + optimalValue + "; " + getAvgSolutionValue() +
                "; " + getRelativeGapToOpt() + "; " + lowerBound + "; " + upperBound +
                "; " + getStandardDeviation() + "; " + getAvgComputationTime());
        writer.write(append);
    }
}