/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author alfredo
 */
class FishServer {
    public ArrayList<Client> clients;
   
    
    
    public FishServer(){
        clients=new ArrayList<Client>();
    }
    
    public String printSummary(){
        String res="";
        for(Client c : clients){
           res+=c.printSummary() + "\n\n\n";
        }
        return res;
    
    }
    
}
