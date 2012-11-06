/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Client {
    private ClientNetworkResources netResources;
    private ArrayList<FishFile> files=new ArrayList<>();    
    
    
    private static final String FILEALREADYINCLIENTLIST="File already in client list!";
    private static final String FILENOTINCLIENTLIST="File not in client list!";
    
    
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    
    
    
    public ClientNetworkResources getNetResources() {
        return netResources;
    }
    
    public void addFile(FishFile s){
        if(!this.files.contains(s)){
            this.files.add(s);
        }
        else{
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, FILEALREADYINCLIENTLIST);
        }
    }
    
    public void removeFile(FishFile s){
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
        res+="Client " + netResources.getSocket().getRemoteSocketAddress() + "\n";
        res+="Files: \n";
        for(FishFile s : files){
            res+=s.getFilename() + " in client " + this.getNetResources().getSocket().getRemoteSocketAddress()+"\n";
            
        }
        return res;
        
    }
    
    
    
    

   
    
    
}
