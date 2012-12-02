/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this payload is exchanged when a server responds to a request of search of a file
 * @author alfredo
 */
public class SearchResult extends Payload  implements Serializable{
    
    private ArrayList<FilenameAndAddress> addresses=new ArrayList<>();

    /**
     *Constructor
     * @param results the list of files found (FilenameAndAddress objects)
     */
    public SearchResult(ArrayList<FilenameAndAddress> results) {
        this.addresses=results;
    }
 

    /**
     * to add a new file to the list
     * @param fr the file to add with its location
     */
    public void addFileResource(FilenameAndAddress fr){
        addresses.add(fr);
    }
    
    /**
     * to get all the files
     * @return the list of files
     */
    public ArrayList<FilenameAndAddress> getFileNamesandAddresses(){
        return this.addresses;
    }
   

    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        String res="File Found in client(s): ";
        for(FilenameAndAddress sa : addresses){
            res+=sa.getAddress().toString() + " " + "File: " + sa.getFilename();
        }
        return res;
    }
    
    
}
