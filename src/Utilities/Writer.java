package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Write a .csv file.
 */
public class Writer {
    /**
     * The file to write.
     */
    private final File file;
    /**
     * The content body of the file.
     */
    private final ArrayList<String> body = new ArrayList<>();
    /**
     * The content header of the file.
     */
    private String head;

    /**
     * Constructor
     *
     * @param directory The directory in which should be written.
     * @param filename  The name of the file.
     */
    public Writer(String directory, String filename) {
        file = new File(directory + filename + ".csv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the file.
     * Overwrites the current content.
     */
    public void write() {
        write(false);
    }

    /**
     * Writes the file.
     *
     * @param append if <CODE>true</CODE> the content will be added at the end of the file,
     *               otherwise it will overwrite the current content
     */
    public void write(boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file, append));
            if (head != null)
                writer.append(head).append("\n");
            for (String line : body) {
                writer.append(line).append("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the header of the file content.
     *
     * @param head The head line of the file content.
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * Adds a record to the content body of the file.
     *
     * @param line The content line which should be added.
     */
    public void addRecordToBody(String line) {
        body.add(line);
    }
}