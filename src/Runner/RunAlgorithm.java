package Runner;

import Utilities.FileLoader;

import java.io.File;

/**
 * Runs a specific algorithm to solve the problem.
 */
public abstract class RunAlgorithm {
    /**
     * The directory of the files.
     */
    protected String directory;

    /**
     * Describes whether summarized file has a head.
     */
    protected boolean summarizedFileHasHead = false;
    /**
     * Number of solutions to build for one problem.
     */
    protected int numOfSolutions;
    /**
     * The optimal value of the problem.
     */
    protected int optimalValue;
    /**
     * The upper bound of the solutions.
     */
    protected int upperBound = Integer.MIN_VALUE;
    /**
     * The lower bound of the solutions.
     */
    protected int lowerBound = Integer.MAX_VALUE;
    /**
     * The sum of all solution values.
     */
    protected double sumSolutionValues = 0;
    /**
     * The sum of all solution computation times.
     */
    protected double sumComputationTime = 0;

    /**
     * Constructor.
     *
     * @param directory      The directory of the files.
     * @param numOfSolutions Number of solutions to build for one problem.
     */
    public RunAlgorithm(String directory, int numOfSolutions) {
        this.directory = directory;
        this.numOfSolutions = numOfSolutions;
    }

    /**
     * Rounds the value to n decimal places.
     *
     * @param value The value to round.
     * @param n     The decimal places
     * @return The rounded value.
     */
    private static double roundToNDecimalPlaces(double value, int n) {
        double scale = Math.pow(10, n);
        return Math.round(value * scale) / scale;
    }

    /**
     * Solves all problems.
     */
    public void solveAllFiles() {
        // load the problem files
        FileLoader fileLoader = new FileLoader(directory + "Problem_Instances/Vrp_Set_A");
        fileLoader.loadFiles(".vrp");

        // solve all files
        for (File file : fileLoader)
            solveOneFile(file);
    }

    /**
     * Solves the problem of the given index.
     */
    public void solveOneFile(int index) {
        // load the problem files
        FileLoader fileLoader = new FileLoader(directory + "Problem_Instances/Vrp_Set_A");
        fileLoader.loadFiles(".vrp");

        solveOneFile(fileLoader.getFile(index));
    }

    /**
     * Solves the problem of the given file.
     */
    public abstract void solveOneFile(File file);

    /**
     * Resets the saved values.
     */
    public void reset() {
        upperBound = Integer.MIN_VALUE;
        lowerBound = Integer.MAX_VALUE;
        sumSolutionValues = 0;
        sumComputationTime = 0;
    }

    /**
     * Sets the optimal value of the problem.
     *
     * @param optimalValue The optimal value.
     */
    public void setOptimalValue(int optimalValue) {
        this.optimalValue = optimalValue;
    }

    /**
     * Sets the bounds of the computed solutions.
     *
     * @param value The computed solution value of the problem.
     */
    public void setBounds(int value) {
        if (value < lowerBound)
            lowerBound = value;
        if (value > upperBound)
            upperBound = value;
    }

    /**
     * Gets the average solution value.
     *
     * @return The average solution value.
     */
    public double getAvgSolutionValue() {
        return sumSolutionValues / numOfSolutions;
    }

    /**
     * Gets the average computation time in seconds.
     * Rounded to four decimal places
     *
     * @return The average computation time in seconds rounded to four decimal places.
     */
    public double getAvgComputationTime() {
        return roundToNDecimalPlaces((sumComputationTime / numOfSolutions) / 1000, 4);
    }

    /**
     * Gets the relative gap between the optimal value and the avg. solution values.
     * Rounded to two decimal places.
     *
     * @return The gap in percent rounded to two decimal places.
     */
    public double getRelativeGapToOpt() {
        double gap = (getAvgSolutionValue() - optimalValue) / optimalValue * 100;
        return roundToNDecimalPlaces(gap, 2);
    }

    /**
     * Gets the relative gap between the lower bound and the upper bound.
     * Rounded to two decimal places.
     *
     * @return The gap in percent rounded to two decimal places.
     */
    public double getRelativeGapBetweenBounds() {
        double gap = (upperBound - lowerBound) / (double) lowerBound * 100;
        return roundToNDecimalPlaces(gap, 2);
    }
}