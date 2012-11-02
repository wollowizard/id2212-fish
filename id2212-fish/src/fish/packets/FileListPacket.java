/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author alfredo
 */
public class FileListPacket implements Serializable {
    private ArrayList<String> filesToAdd=new ArrayList<>();
    private ArrayList<String> filesToRemove=new ArrayList<>();
    
    public FileListPacket(ArrayList<File> add, ArrayList<File> remove){
        for(File f : add){
            filesToAdd.add(f.getPath());
        }
        for(File f : remove){
            filesToRemove.add(f.getPath());
        }
    }



    public ArrayList<String> getFilesToAdd() {
        return filesToAdd;
    }
    
    public ArrayList<String> getFilesToRemove() {
        return filesToRemove;
    }

    public String printSummary() {
        String res= "Files to add: ";
        for(String s : filesToAdd){
            res+=s+", ";
        }
        
        res+= "\nFiles to remove: ";
        for(String s : filesToRemove){
            res+=s+", ";
        }
        res+="\n\n";
        
        return res;
    }
    
    
}
