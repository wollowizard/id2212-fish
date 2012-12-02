package fish.client.dir;

import java.io.*;

/**
 * This class implements a file filter, so it is used to select just some files
 * @author alfredo
 */
public class DirFilterWatcher implements FileFilter {

    private String filter;

    /**
     * Constructor (sets the default filter as no filter)
     */
    public DirFilterWatcher() {
        this.filter = "";
    }

    /**
     * Constructor
     * @param filter
     */
    public DirFilterWatcher(String filter) {
        this.filter = filter;
    }

    /**
     * accepts a file if it ends with the filter
     * @param file the file to accept or reject
     * @return true if accepted
     */
    @Override
    public boolean accept(File file) {
        if ("".equals(filter) || "*".equals(filter)) {
            return true;
        }
        return (file.getName().endsWith(filter));
    }
}