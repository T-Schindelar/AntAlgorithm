package Runner;

import NearestNeighborHeuristic.NearestNeighborHeuristic;
import Utilities.ProblemInstance;
import Utilities.Writer;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * Runs the Nearest Neighbor Heuristic to solve the problem.
 */
public class RunNNH extends RunAlgorithm {
    /**
     * Constructor.
     *
     * @param directory The directory of the files.
     * @param dataSet   The dataset to solve.
     */
    public RunNNH(String directory, String dataSet) {
        super(directory, dataSet, 1);
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
        Writer writer = new Writer(directory + "Detailed_Results/NNH/", problem.getName());
        writer.setHead("problem; optimal value");
        writer.addRecordToBody(problem.getName() + "; " + optimalValue);
        writer.addRecordToBody("");
        writer.addRecordToBody("solution value; gap in %; computation time in sec");

        // console output as user information
        System.out.println("Solving " + problem.getName() + ", please wait...");

        // initialize the nearest neighbor heuristic
        NearestNeighborHeuristic nnh = new NearestNeighborHeuristic(problem);

        // solve and measure time
        Instant start = Instant.now();
        nnh.solve();
        Instant finish = Instant.now();
        long compTime = Duration.between(start, finish).toMillis();

        // save result
        sumSolutionValues += nnh.getTourLength();
        sumComputationTime += compTime;

        // write result
        writer.addRecordToBody((int) nnh.getTourLength() + "; " + getRelativeGapToOpt() + "; "
                + getAvgComputationTime());
        writer.write();

        // write summary
        writer = new Writer(directory + "Summarized_Results/", "summarized_results_nnh");
        boolean append = true;

        // resets the file and sets the heading
        if (!summarizedFileHasHead) {
            writer.setHead("problem; optimal value; result; gap in %; avg. comp. time");
            summarizedFileHasHead = true;
            append = false;
        }

        writer.addRecordToBody(problem.getName() + "; " + optimalValue + "; " + getAvgSolutionValue() +
                "; " + getRelativeGapToOpt() + "; " + getAvgComputationTime());
        writer.write(append);
    }
}