/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author alfredo
 */
public class SearchResult extends Payload  implements Serializable{
    
    private ArrayList<FilenameAndAddress> addresses=new ArrayList<>();

    /**
     *
     * @param results
     */
    public SearchResult(ArrayList<FilenameAndAddress> results) {
        this.addresses=results;
    }
 

    /**
     *
     * @param fr
     */
    public void addFileResource(FilenameAndAddress fr){
        addresses.add(fr);
    }
    
    /**
     *
     * @return
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
