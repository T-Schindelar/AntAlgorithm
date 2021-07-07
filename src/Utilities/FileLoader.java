package Utilities;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

/**
 * The FileLoader class loads files from a specific directory and stores them in a File array.
 */
public class FileLoader implements Iterable<File> {
    /**
     * Directory of the files.
     */
    private final String directory;

    /**
     * List of files.
     */
    private File[] files;

    /**
     * Constructor.
     *
     * @param directory The directory of the files
     */
    public FileLoader(String directory) {
        this.directory = directory;
    }

    /**
     * Loads all files from the directory.
     *
     * @return <CODE>true</CODE> if the load was successful, <CODE>false</CODE> otherwise
     */
    public boolean loadFiles() {
        return loadFiles("");
    }

    /**
     * Loads all files with a specific ending from the directory.
     *
     * @param endsWith The ending of the files (e.g. ".txt").
     * @return <CODE>true</CODE> if the load was successful, <CODE>false</CODE> otherwise
     */
    public boolean loadFiles(String endsWith) {
        try {
            files = new File(directory).listFiles((dir, f) -> f.toLowerCase().endsWith(endsWith));
            assert files != null;
            Arrays.sort(files);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Gets a specific file from files[] at the index position.
     *
     * @param index Index position of the file.
     * @return The specific file.
     */
    public File getFile(int index) {
        return files[index];
    }

    /**
     * Makes class iterable.
     *
     * @return An Iterator for the class.
     */
    @Override
    public Iterator<File> iterator() {
        return Arrays.stream(files).iterator();
    }
}