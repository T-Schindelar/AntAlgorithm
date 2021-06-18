package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// TODO: 18.06.21 add documentation
/**
 * Write a .csv file.
 */
public class Writer {
    private final File file;
    private final ArrayList<String> body = new ArrayList<>();
    private String head;

    public Writer(String directory, String filename) {
        file = new File(directory + filename + ".csv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write() {
        write(false);
    }

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

    public void setHead(String head) {
        this.head = head;
    }

    public void addRecordToBody(String line) {
        body.add(line);
    }
}