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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alfredo
 */
class FishServer {

    public ArrayList<Client> clients;
    public ArrayList<FishFile> files;

    public FishServer() {
        clients = new ArrayList<Client>();
        files = new ArrayList<FishFile>();
    }

    public void newClientConnected(Client c) {
        if (!clients.contains(c)) {
            this.clients.add(c);
        }
    }

    public void clientDisconnected(Client client) {
        ArrayList<FishFile> toberemoved=new ArrayList<>();
        for(FishFile f : files){
            if(f.getOwner().equals(client)){
                toberemoved.add(f);
            }
        }
        for(FishFile f : toberemoved){
            files.remove(f);
        }
        
        client.clearFiles();
        clients.remove(client);
    }

    public void updateFilesOfClient(ArrayList<FishFile> add, ArrayList<FishFile> remove, Client client) {
        System.out.println((new Date()).toString() + " Received packet: ");
        //System.out.println(fp.printSummary());


        for (FishFile s : add) {
            
            if(addFile(client, s)){
                client.addFile(s);
            }

        }

        for (FishFile s : remove) {
            client.removeFile(s);
            removeFile(client, s);
        }



        System.out.println("\n\n" + client.printSummary() + "\n\n");
    }

    public FishPacket search(Client c, String parameter) {
        
        Header header=null; 
       // ArrayList<Client> clientswithfile=new ArrayList<>();
       Map <Client,FilenameAndAddress> clients=new HashMap<>();
        
        for (int i = 0; i < files.size(); i++) {
            FishFile f = files.get(i);
            if (f.getFilename().contains(parameter)) {
                //if(!clientswithfile.contains(f.getOwner())){
                if(clients.get(f.getOwner())==null){//-------------------------------
                   //clientswithfile.add(f.getOwner());
                   clients.put(f.getOwner(), new FilenameAndAddress(f.getFilename(), f.getOwnerRemoteAddress()));
                }
            }
        }
        
        
        clients.remove(c);
        //clientswithfile.remove(c);
        
       // if(clientswithfile.size()==0){
         if(clients.isEmpty()){
            header = new Header(PacketType.FILENOTFOUND);
        }
        else{
            header = new Header(PacketType.FILEFOUND);
        }
        
       
        SearchResult sr = new SearchResult();
        //for(Client i : clientswithfile){
        
        for (Map.Entry<Client, FilenameAndAddress> i : clients.entrySet()){
            FilenameAndAddress fr=new FilenameAndAddress(i.getValue().getFilename(), i.getKey().getNetResources().getSocket().getRemoteSocketAddress());
            sr.addFileResource(fr);
        }
        
        FishPacket fp=new FishPacket(header, sr);
        return fp;

    }

    private boolean addFile(Client c, FishFile file) {
        boolean found=false;
        for(int i=0;i<this.files.size() && !found;i++){
            if( file.getFilename().compareTo(files.get(i).getFilename())==0
                    && c==files.get(i).getOwner() ){
                found=true;
            }
        }
        if(!found){
            this.files.add(file);
        }
        return !found;

    }

    private void removeClient(Client c) {
        this.clients.remove(c);
    }

    private void removeFile(Client c, FishFile file) {

        this.files.remove(file);

    }

    public String printSummary() {
        String res = "";
        for (Client c : clients) {
            res += c.printSummary() + "\n\n\n";
        }
        return res;

    }
}
