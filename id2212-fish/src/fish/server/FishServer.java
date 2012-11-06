/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FileList;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import fish.packets.SearchResult;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

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

    public void addFilesOfClient(FileList fp, Client client) {
        System.out.println((new Date()).toString() + " Received packet: ");
        System.out.println(fp.printSummary());

        ArrayList<String> toAdd = fp.getFilesToAdd();
        ArrayList<String> toRemove = fp.getFilesToRemove();

        for (String s : toAdd) {
            client.addFile(s);
            addFile(client, s);

        }

        for (String s : toRemove) {
            client.removeFile(s);
            removeFile(client, s);
        }



        System.out.println("\n\n" + client.printSummary() + "\n\n");
    }

    public FishPacket search(Client c, String parameter) {
        
        Header header=null; 
        ArrayList<Client> clientswithfile=new ArrayList<>();
        
        for (int i = 0; i < files.size(); i++) {
            FishFile f = files.get(i);
            if (f.getFilename().contains(parameter)) {
                if(!clientswithfile.contains(f.getOwner())){
                    clientswithfile.add(f.getOwner());
                }
                
            }
        }
        
        clientswithfile.remove(c);
        
        if(clientswithfile.size()==0){
            header = new Header(PacketType.FILENOTFOUND);
        }
        else if(clientswithfile.size()>=1){
            header = new Header(PacketType.FILEFOUND);
        }
        
        
        
       
        SearchResult sr = new SearchResult();
        for(Client i : clientswithfile){
            sr.addAddress(i.getNetResources().getSocket().getRemoteSocketAddress());
        }
        
        FishPacket fp=new FishPacket(header, sr);
        return fp;

    }

    private void addFile(Client c, String filename) {

        this.files.add(new FishFile(filename, c));

    }

    private void removeClient(Client c) {
        this.clients.remove(c);
    }

    private void removeFile(Client c, String filename) {

        this.files.remove(new FishFile(filename, c));

    }

    public String printSummary() {
        String res = "";
        for (Client c : clients) {
            res += c.printSummary() + "\n\n\n";
        }
        return res;

    }
}
