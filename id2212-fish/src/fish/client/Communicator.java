/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FileList;
import fish.packets.FishPacket;
import fish.packets.PacketType;
import fish.packets.Payload;
import fish.packets.SearchResult;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Communicator extends Thread {

    private FishPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;

    public Communicator(FishPacket p, ObjectInputStream in, ObjectOutputStream out, Client client) {

        this.packet = p;
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {


        try {
            synchronized (client) {

                System.out.println("\n\nCOMMUNICATOR!!\n\n");
                System.out.println("Sending: ");
                System.out.println(this.packet.getPayload().printSummary() + "\n\n");

                out.toString();

                out.writeObject(this.packet);
                out.flush();
                out.reset();//important!!!!!!!! we always send the same obj, but fields are changed
                
                if(packet.getHeader().getType()==PacketType.ADDFILE){
                    client.filesToAdd.clear();
                    client.filesToRemove.clear();
                }
                
                
                else if(packet.getHeader().getType()==PacketType.SEARCH){
                    try {
                        FishPacket fp=(FishPacket)in.readObject();
                        if(fp.getHeader().getType()==PacketType.FILENOTFOUND){
                            System.out.println("FILE NOT FOUND!!!!!");
                        }
                        else if(fp.getHeader().getType()==PacketType.FILEFOUND){
                            System.out.print("FILE FOUND !!!!! ");
                            SearchResult results = (SearchResult) fp.getPayload();
                            System.out.println(results.printSummary());
                            
                        }
                        else if(fp.getHeader().getType()==PacketType.FILEFOUNDBUTYOUOWNIT){
                            System.out.println("FILE FOUND but it's already yours!!!!!");
                            SearchResult results = (SearchResult) fp.getPayload();
                            System.out.println(results.printSummary());
                        }
                        
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                
                }
                
            }

        } catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
