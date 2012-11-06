/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FishPacket;
import fish.packets.PacketType;
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
public class Sender extends Thread {

    private FishPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;

    public Sender(FishPacket p, ObjectInputStream in, ObjectOutputStream out, Client client) {

        this.packet = p;
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            synchronized (client) {

                System.out.println("\n\nSENDER!!\n\n");
                System.out.println("Sending: ");
                System.out.println(this.packet.getPayload().printSummary() + "\n\n");

                out.toString();

                out.writeObject(this.packet);
                out.flush();
                out.reset();//important!!!!!!!! we always send the same obj, but fields are changed

                if (packet.getHeader().getType() == PacketType.ADDFILE) {
                    client.filesToAdd.clear();
                    client.filesToRemove.clear();
                    
                    
                } else if (packet.getHeader().getType() == PacketType.SEARCH) {
                    //if it was a search, we should wait for a resonse
                    
                    Receiver recThread = new Receiver(packet, in, out, client);
                    recThread.start();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
