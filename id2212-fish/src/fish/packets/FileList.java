/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * contains to array of files, one for files to add (already in the 
 * client but not yet in the server) and one for the files to remove (removed 
 * by the client but still present in the server). Used to sync the list of
 * files in client and server
 * @author alfredo
 */
public class FileList extends Payload implements Serializable {

    private ArrayList<String> filesToAdd = new ArrayList<>();
    private ArrayList<String> filesToRemove = new ArrayList<>();

    /**
     * constructor
     * @param add list of files to add to the server list
     * @param remove list of files to remove from the server list
     */
    public FileList(ArrayList<File> add, ArrayList<File> remove) {
        for (File f : add) {

            filesToAdd.add(f.getName());
        }
        for (File f : remove) {
            filesToRemove.add(f.getName());
        }
    }

    /**
     * 
     * @return
     */
    public ArrayList<String> getFilesToAdd() {
        return filesToAdd;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getFilesToRemove() {
        return filesToRemove;
    }

    /**
     * prints all the files
     * @return all the file names as strings
     */
    @Override
    public String printSummary() {
        String res = "Files to add: ";
        for (String s : filesToAdd) {
            res += s + ", ";
        }

        res += "\nFiles to remove: ";
        for (String s : filesToRemove) {
            res += s+", ";
        }
        res += "\n\n";

        return res;
    }


}
