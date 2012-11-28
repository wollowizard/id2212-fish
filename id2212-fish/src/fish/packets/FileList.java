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
 *
 * @author alfredo
 */
public class FileList extends Payload implements Serializable {

    private ArrayList<String> filesToAdd = new ArrayList<>();
    private ArrayList<String> filesToRemove = new ArrayList<>();

    /**
     *
     * @param add
     * @param remove
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
     *
     * @return
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

    /**
     *
     * @param file
     * @return
     */
    public String calculateMd5(File file) {
        InputStream is = null;
        String res="";
        try {
            is = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024]; // or any other size
            int len;
            while ((len = is.read(buffer)) != -1) {
                md5.update(buffer, 0, len); // only update with the just read bytes  
            }
            byte[] result = md5.digest(); // finish up
            BigInteger bi = new BigInteger(1, result);
            is.close();
            res=String.format("%0" + (result.length << 1) + "X", bi);
        } catch (IOException ex) {
            Logger.getLogger(FileList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(FileList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }

}
