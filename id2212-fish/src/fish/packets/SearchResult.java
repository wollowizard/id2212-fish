/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.net.SocketAddress;
import java.util.ArrayList;

/**
 *
 * @author alfredo
 */
public class SearchResult extends Payload{
    
    public ArrayList<SocketAddress> addresses=new ArrayList<>();

    public void addAddress(SocketAddress sa){
        addresses.add(sa);
    }
    
   

    @Override
    public String printSummary() {
        String res="File Found in client(s): ";
        for(SocketAddress sa : addresses){
            res+=sa.toString() + " ";
        }
        return res;
    }
    
    
}
