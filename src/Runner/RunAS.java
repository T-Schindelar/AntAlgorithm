package Runner;

import AntColonyOptimization.AntSystem;
import Utilities.ProblemInstance;
import Utilities.Writer;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * Runs the Ant System algorithm to solve the problem.
 */
public class RunAS extends RunAlgorithm {
    /**
     * Alpha: the relative importance of the trail.
     */
    protected double alpha;
    /**
     * Beta: the relative importance of the visibility.
     */
    protected double beta;
    /**
     * Rho: trail persistence (1 - ρ can be interpreted as trail evaporation).
     */
    protected double rho;
    /**
     * The initial tau value for the pheromone matrix.
     */
    protected double tau0;
    /**
     * Number of iterations.
     */
    protected int numIt;

    /**
     * Constructor.
     *
     * @param directory      The directory of the files.
     * @param numOfSolutions Number of solutions to build for one problem.
     * @param alpha          The alpha value.
     * @param beta           The beta value.
     * @param rho            The rho value.
     * @param tau0           The tau0 value.
     * @param numIt          The number of iterations value.
     */
    public RunAS(String directory, int numOfSolutions, double alpha, double beta, double rho, double tau0, int numIt) {
        super(directory, numOfSolutions);
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.tau0 = tau0;
        this.numIt = numIt;
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
        Writer writer = new Writer(directory + "Detailed_Results/AS/", problem.getName());
        writer.setHead("problem; " + "alpha" + "; " + "beta" + "; " + "rho" + "; " + "tau0" + "; " + "iterations");
        writer.addRecordToBody(problem.getName() + "; " + alpha + "; " + beta + "; " + rho + "; " + tau0 + "; " +
                numIt);
        writer.addRecordToBody("");
        writer.addRecordToBody("nr; solution value; computation time in sec");

        // console output as user information
        System.out.println("Solving " + problem.getName() + ", please wait...");

        // solve the problem numOfSolutions times
        for (int i = 0; i < numOfSolutions; i++) {
            // initialize the Ant System
            AntSystem antSystem = new AntSystem(problem);

            // set parameter
            antSystem.setAlpha(alpha);
            antSystem.setBeta(beta);
            antSystem.setRho(rho);
            antSystem.setInitialTau(tau0);
            antSystem.setNumberOfIterations(numIt);

            // TODO: 13.06.21 Zeitmessung anpassen ms oder s
            // solve and measure time
            Instant start = Instant.now();
            antSystem.solve();
            Instant finish = Instant.now();
            long compTime = Duration.between(start, finish).toMillis();

            // save results
            solutionValues.add(antSystem.getBestTourLength());
            sumSolutionValues += antSystem.getBestTourLength();
            sumComputationTime += compTime;

            // set bounds
            setBounds((int) antSystem.getBestTourLength());

            // write result
            writer.addRecordToBody(i + 1 + "; " + (int) antSystem.getBestTourLength() + "; "
                    + compTime / 1000d);
        }
        writer.addRecordToBody("");
        writer.addRecordToBody("avg; " + getAvgSolutionValue() + "; " + getAvgComputationTime());
        writer.write();

        // write summary
        writer = new Writer(directory + "Summarized_Results/", "summarized_results_as");
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