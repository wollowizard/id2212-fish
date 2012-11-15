/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.File;
import java.nio.file.NotDirectoryException;

/**
 *
 * @author alfredo
 */
public class FileWalker {

    private Client client;

    public FileWalker(Client c) {
        this.client = c;
    }

    public void walk(String path) throws NotDirectoryException {

        try {
            File root = new File(path);
            if (!root.isDirectory()) {
                throw new NotDirectoryException("Invalid directory");
            }
            File[] list = root.listFiles();
            for (File f : list) {
                if (f.isDirectory()) {
                    walk(f.getAbsolutePath());
                    System.out.println("Dir:" + f.getAbsoluteFile());
                    client.addWatcher(f.getAbsolutePath());
                } else {
                    System.out.println("File:" + f.getAbsoluteFile());
                    client.addFile(f);
                }
            }

            client.sendFileList();
        } catch (Exception ex) {
            throw new NotDirectoryException("Invalid directory");
        }


    }
}
