/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import fish.packets.FishPacket;
import fish.packets.PacketType;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The thread in charge to send packets
 * @author alfredo
 */
public class Sender extends Thread {

    private FishPacket packet;
    
    private ObjectOutputStream out;
    private ClientController client;

    /**
     *
     * @param p the packet to send
     * @param out the output stream
     * @param client the client controller
     */
    public Sender(FishPacket p, ObjectOutputStream out, ClientController client) {

        this.packet = p;
        
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            synchronized (client) {

                
                System.out.println("Sending: ");
                System.out.println(this.packet.printSummary());

                out.toString();

                out.writeObject(this.packet);
                out.flush();
                out.reset();//important!!!!!!!! we always send the same obj, but fields are changed

                if (packet.getHeader().getType() == PacketType.ADDFILE) {
                    client.clearFilesToAdd();
                    client.clearFilesToRemove();
                }
            }

        } catch (IOException ex) {
            client.setDisconnected();
            
        }
    }
}
