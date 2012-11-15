/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.dir;

import fish.client.Client;
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
    
    public static File findFile(String filename, String rootDir)throws NotDirectoryException{
        File toreturn=null;
        try {
            File root = new File(rootDir);
            if (!root.isDirectory()) {
                throw new NotDirectoryException("Invalid directory");
            }
            File[] list = root.listFiles();
            for (File f : list) {
                if (f.isDirectory()) {
                    findFile(filename,f.getAbsolutePath());
                    System.out.println("Dir:" + f.getAbsoluteFile());
                    
                } else {
                    System.out.println("File:" + f.getAbsoluteFile());
                    if(filename.compareTo(f.getName())==0){
                        System.out.println("found!!");
                        toreturn=f;
                        break;
                    }
                    
                }
            }

            
        } catch (Exception ex) {
            throw new NotDirectoryException("Invalid directory");
        }
        return toreturn;
        
    
    }
}