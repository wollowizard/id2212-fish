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
public class Receiver extends Thread {

    private FishPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;

    public Receiver(FishPacket p, ObjectInputStream in, ObjectOutputStream out, Client client) {

        this.packet = p;
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {


        try {
            synchronized (client) {

                System.out.println("\n\nRECEIVER!!\n\n");
                FishPacket fp = (FishPacket) in.readObject();
                if (packet.getHeader().getType() == PacketType.SEARCH) {

                    if (fp.getHeader().getType() == PacketType.FILENOTFOUND) {
                        System.out.println("FILE NOT FOUND!!!!!");
                    } else if (fp.getHeader().getType() == PacketType.FILEFOUND) {
                        System.out.print("FILE FOUND !!!!! ");
                        SearchResult results = (SearchResult) fp.getPayload();
                        System.out.println(results.printSummary());

                    } else if (fp.getHeader().getType() == PacketType.FILEFOUNDBUTYOUOWNIT) {
                        System.out.println("FILE FOUND but it's already yours!!!!!");
                        SearchResult results = (SearchResult) fp.getPayload();
                        System.out.println(results.printSummary());
                    }

                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
