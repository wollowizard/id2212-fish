/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.dir;

import fish.client.controller.ClientController;
import fish.exceptions.NotDirectoryException;
import java.io.File;

/**
 * This class walks through a directory tree
 * @author alfredo
 */
public class FileWalker {

    private ClientController client;

    /**
     * Constructor
     * @param c the client controller
     */
    public FileWalker(ClientController c) {
        this.client = c;
    }

    /**
     * The main method, walks thorugh the directory tree and adds other watchers to directories, and adds or removes files
     * @param path
     * @throws NotDirectoryException
     */
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

    /**
     * Returns a file given its exact name. Looks into subfolders. it returns the first file
     * @param filename the exact file name
     * @param rootDir The folder to inspect
     * @return The file found or null
     * @throws NotDirectoryException if a non valid directory is given
     */
    public static File findFile(String filename, String rootDir) throws NotDirectoryException {

        try {
            File root = new File(rootDir);
            if (!root.isDirectory()) {
                throw new NotDirectoryException("Invalid directory");
            }
            File[] list = root.listFiles();
            for (File f : list) {
                if (f.isDirectory() ) {
                    File x = findFile(filename, f.getAbsolutePath());
                    if( x!=null){
                        return x;
                    }
                    //System.out.println("Dir:" + f.getAbsoluteFile());

                } else {
                    System.out.println("File:" + f.getAbsoluteFile());
                    if (filename.compareTo(f.getName()) == 0) {
                        System.out.println("found :" + f.getAbsoluteFile());
                        return f;
                    }

                }
            }
        } catch (Exception ex) {
            throw new NotDirectoryException("Invalid directory");
        }
        return null;

    }
}
