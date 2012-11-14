/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.PacketType;
import fish.packets.SearchResult;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Receiver extends Thread {

    //private FishPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;
    private Boolean running = true;

    public Receiver(ObjectInputStream in, ObjectOutputStream out, Client client) {

        //this.packet = p;
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {


        try {


            System.out.println("\n\nRECEIVER!!\n\n");
            while (running) {
                FishPacket fp = (FishPacket) in.readObject();
               // synchronized (client) {

                   this.manageSearchResponse(fp);

                //}

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void manageSearchResponse(FishPacket received) {
        if (received.getHeader().getType() == PacketType.FILENOTFOUND) {
            System.out.println("FILE NOT FOUND!!!!!");

            this.client.setLastresult(new ArrayList<FilenameAndAddress>());


        } else if (received.getHeader().getType() == PacketType.FILEFOUND) {
            System.out.print("FILE FOUND !!!!! ");
            SearchResult results = (SearchResult) received.getPayload();
            this.client.setLastresult(results.getFileNamesandAddresses());

        }
        else if(received.getHeader().getType() == PacketType.DOWNLOAD){
            
        
        }
    }
}
