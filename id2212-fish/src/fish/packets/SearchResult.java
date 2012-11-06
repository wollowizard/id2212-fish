/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 *
 * @author alfredo
 */
public class SearchResult extends Payload  implements Serializable{
    
    public ArrayList<FilenameAndAddress> addresses=new ArrayList<>();

    public void addFileResource(FilenameAndAddress fr){
        addresses.add(fr);
    }
    
   

    @Override
    public String printSummary() {
        String res="File Found in client(s): ";
        for(FilenameAndAddress sa : addresses){
            res+=sa.getAddress().toString() + " " + "File: " + sa.getFilename();
        }
        return res;
    }
    
    
}
