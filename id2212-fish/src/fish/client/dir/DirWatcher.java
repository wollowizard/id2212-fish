package fish.client.dir;

import java.io.*;
import java.util.*;

/**
 * Implements a TimerTask, it watches a folder to look for modifications at its files
 * @author alfredo
 */
public abstract class DirWatcher extends TimerTask {

    private String path;
    private File filesArray[];
    private HashMap dir = new HashMap();
    private DirFilterWatcher dfw;

    /**
     * Constructor
     * @param path the folder to watch
     * @param filter the filter to exclude some files
     */
    public DirWatcher(String path, String filter) {

        this.path = path;
        dfw = new DirFilterWatcher(filter);
        filesArray = new File(path).listFiles(dfw);

        // transfer to the hashmap be used a reference and keep the
        // lastModfied value
        for (int i = 0; i < filesArray.length; i++) {
            dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
        }
    }

    @Override
    public final void run() {


        HashSet checkedFiles = new HashSet();
        filesArray = new File(path).listFiles(dfw);

        // scan the files and check for modification/addition
        for (int i = 0; i < filesArray.length; i++) {
            Long current = (Long) dir.get(filesArray[i]);
            checkedFiles.add(filesArray[i]);
            if (current == null) {
                // new file
                dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
                onChange(filesArray[i], "add");
            } else if (current.longValue() != filesArray[i].lastModified()) {
                // modified file
                dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
                onChange(filesArray[i], "modify");
            }

        }

        // now check for deleted files
        Set ref = ((HashMap) dir.clone()).keySet();
        ref.removeAll((Set) checkedFiles);
        Iterator it = ref.iterator();

        while (it.hasNext()) {
            File deletedFile = (File) it.next();
            dir.remove(deletedFile);
            onChange(deletedFile, "delete");


        }
    }

    /**
     *
     * @param file
     * @param action
     */
    protected abstract void onChange(File file, String action);
}