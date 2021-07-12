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
        writer.addRecordToBody("nr; solution value; gap in %; computation time in sec");

        // console output as user information
        System.out.println("Solving " + problem.getName() + ", please wait...");

        // initialize the nearest neighbor heuristic
        NearestNeighborHeuristic nnh = new NearestNeighborHeuristic(problem);

        // TODO: 13.06.21 Zeitmessung anpassen ms oder s
        // solve and measure time
        Instant start = Instant.now();
        nnh.solve();
        Instant finish = Instant.now();
        long compTime = Duration.between(start, finish).toMillis();

        // save result
        sumSolutionValues += nnh.getTourLength();

        // write result
        writer.addRecordToBody("1; " + (int) nnh.getTourLength() + "; " + getRelativeGapToOpt() + "; " +
                compTime);
        writer.write();
    }
}