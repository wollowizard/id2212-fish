/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import fish.packets.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author alfredo
 */
class FishServer {

    //public ArrayList<Client> clients;
    //public ArrayList<FishFile> files;
    
    Map<FishFile,Client> filesMap=new ConcurrentHashMap<>();
    List clients = Collections.synchronizedList(new ArrayList()); 

  
    public void newClientConnected(Client c) {
        if (!clients.contains(c)) {
            this.clients.add(c);
        }
    }

    public void clientDisconnected(Client client) {
        ArrayList<Map.Entry<FishFile,Client>> toberemoved=new ArrayList<>();
        
        for (Map.Entry<FishFile,Client> entry : filesMap.entrySet()){
            if(entry.getValue().equals(client)){
                toberemoved.add(entry);
            }
        }
                
        for(Map.Entry<FishFile,Client> f : toberemoved){
            filesMap.remove(f.getKey());
        }
        
        client.clearFiles();
        clients.remove(client);
    }
    
   
    
    public void updateFilesOfClient(ArrayList<FishFile> add, ArrayList<FishFile> remove, Client client) {

        for (FishFile s : add) {
       
            if(addFile(client, s)){
                client.addFile(s);
            }
        }

        for (FishFile s : remove) {
            client.removeFile(s);
            removeFile(client, s);
        }

    }

    public FishPacket search(Client c, String parameter) {
        
        ArrayList<Map.Entry<FishFile,Client>> results=new ArrayList<>();
        
        for (Map.Entry<FishFile,Client> entry : filesMap.entrySet()){
            if(entry.getKey().getFilename().contains(parameter) && 
                    (!entry.getValue().equals(c))){
                results.add(entry);
            }
        }
       
        Header header=null;
        
         if(results.isEmpty()){
            header = new Header(PacketType.FILENOTFOUND);
        }
        else{
            header = new Header(PacketType.FILEFOUND);
        }
        
       
        SearchResult sr = new SearchResult();
        
        
        for (Map.Entry<FishFile, Client> i : results){
            FilenameAndAddress fr=new FilenameAndAddress(i.getKey().getFilename(), i.getValue().getNetResources().getSocket().getRemoteSocketAddress());
            sr.addFileResource(fr);
        }
        
        FishPacket fp=new FishPacket(header, sr);
        return fp;

    }

    private boolean addFile(Client c, FishFile file) {
        boolean found=false;
        
        for (Map.Entry<FishFile,Client> entry : filesMap.entrySet()){
            if(found){
                break;
            }
            FishFile fileInMap=entry.getKey();
            if((fileInMap.getFilename().compareTo(file.getFilename())==0)&&
                    fileInMap.getOwner().equals(c)){
                found=true;
            }
        }
        
        if(!found){
            this.filesMap.put(file,c);
        }
        return !found;

    }

    private void removeClient(Client c) {
        this.clients.remove(c);
    }

    private void removeFile(Client c, FishFile file) {

        this.filesMap.remove(file);

    }

    public String printSummary() {
        String res = "";
        for (Object o : clients) {
            Client c = (Client)o;
            res += c.printSummary() + "\n\n\n";
        }
        return res;

    }
}
