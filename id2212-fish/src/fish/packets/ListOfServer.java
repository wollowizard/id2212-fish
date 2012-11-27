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
public class ListOfServer extends Payload implements Serializable {
    
    public ArrayList<Server> servers;
    
    public ListOfServer(ArrayList<Server> arr){
        this.servers=arr;
    }

    @Override
    public String printSummary() {
        String res="List of servers: \n";
        for(Server s : servers){
            res+= s.getId() + " " + " " + s.getAddress() + ":" + s.getPortForClients().toString() + "\n";
        }
        return res;
    }
    
    
    
}
