package fish.client.dir;

import java.io.*;

/**
 *
 * @author alfredo
 */
public class DirFilterWatcher implements FileFilter {

    private String filter;

    /**
     *
     */
    public DirFilterWatcher() {
        this.filter = "";
    }

    /**
     *
     * @param filter
     */
    public DirFilterWatcher(String filter) {
        this.filter = filter;
    }

    @Override
    public boolean accept(File file) {
        if ("".equals(filter) || "*".equals(filter)) {
            return true;
        }
        return (file.getName().endsWith(filter));
    }
}