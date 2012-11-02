/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Client {
    private ClientNetworkResources netResources;
    private static final String FILEALREADYINCLIENTLIST="File already in client list!";
    private static final String FILENOTINCLIENTLIST="File not in client list!";
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    private Vector<String> files=new Vector<>();
    
    
    public ClientNetworkResources getNetResources() {
        return netResources;
    }
    
    public void addFile(String s){
        if(!this.files.contains(s)){
            this.files.add(s);
        }
        else{
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, FILEALREADYINCLIENTLIST);
        }
    }
    
    public void removeFile(String s){
        if(this.files.contains(s)){
            this.files.remove(s);
        }
        else{
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, FILENOTINCLIENTLIST);
        }
    
    }

    public void clearFiles() {
        this.files.clear();
    }
    
    public String printSummary(){
        String res="";
        res+="Client " + netResources.getSocket().getInetAddress().getCanonicalHostName() + "\n";
        res+="Files: \n";
        for(String s : files){
            res+=s + ", ";
        }
        return res;
        
    }
    
    
    
    

   
    
    
}
