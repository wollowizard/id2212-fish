/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import fish.server.Client;
import fish.server.FishFile;
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

    public FileList(ArrayList<File> add, ArrayList<File> remove) {
        for (File f : add) {

            filesToAdd.add(new String(f.getName()));
        }
        for (File f : remove) {
            filesToRemove.add(f.getName());
        }
    }

    public ArrayList<String> getFilesToAdd() {
        return filesToAdd;
    }

    public ArrayList<String> getFilesToRemove() {
        return filesToRemove;
    }

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

    public ArrayList<FishFile> getListOfFishFilesToAdd(Client client) {
        ArrayList<FishFile> ret=new ArrayList<>();
        for(String fnwh : filesToAdd){
            FishFile ff=new FishFile(fnwh, client);
            ret.add(ff);
        }
        return ret;
    }
    public ArrayList<FishFile> getListOfFishFilesToRemove(Client client) {
        ArrayList<FishFile> ret=new ArrayList<>();
        for(String fnwh : filesToRemove){
            FishFile ff=new FishFile(fnwh, client);
            ret.add(ff);
        }
        return ret;
    }
}
