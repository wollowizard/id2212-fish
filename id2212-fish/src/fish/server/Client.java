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
    private Integer listeningServerPort=-1;
    
    
    public String getClientName() {
        return this.netResources.getSocket().getRemoteSocketAddress().toString();
    }
    
    
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    public synchronized ClientNetworkResources getNetResources() {
        return netResources;
    }
    
    public synchronized void addFile(FishFile s){
        if(!this.files.contains(s)){
            this.files.add(s);
        }
        else{
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, FILEALREADYINCLIENTLIST);
        }
    }
    
    public synchronized void removeFile(FishFile s){
        FishFile toremove=null;
        for(FishFile ff : this.files){
            if(ff.getFilename().compareTo(s.getFilename())==0){
                if(ff.getOwner()==s.getOwner()){
                    
                    toremove=ff;
                    break;
                    
                }
            }
        }
        if(toremove!=null){
            this.files.remove(toremove);
        }
        
        else{
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, FILENOTINCLIENTLIST);
        }
    
    }

    public synchronized  void clearFiles() {
        this.files.clear();
    }
    
    public synchronized String printSummary(){
        String res="";
        res+="Client " + netResources.getSocket().getRemoteSocketAddress() + "\n";
        res+="Files: \n";
        for(FishFile s : files){
            res+=s.getFilename() + " in client " + this.getNetResources().getSocket().getRemoteSocketAddress()+"\n";
            
        }
        return res;
        
    }

    void setListeningServerPort(Integer port) {
        this.listeningServerPort=port;
    }

    Integer getListeningServerPort() {
        return this.listeningServerPort;
    }
    
   
 
    
    
}
