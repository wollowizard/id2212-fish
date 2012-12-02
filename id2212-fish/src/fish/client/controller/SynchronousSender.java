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
 * A synchronous sender (not a thread) 
 * @author alfredo
 */
class SynchronousSender {

    private FishPacket packet;
    private ObjectOutputStream out;
    private ClientController client;

    
    /**
     * The constructor
     * @param p the packet to send
     * @param out the output stream of the socket
     * @param client the client controller
     */
    public SynchronousSender(FishPacket p, ObjectOutputStream out, ClientController client) {

        this.packet = p;

        this.out = out;
        this.client = client;
    }

    /**
     * the method starts the send operation
     */
    public void send() {
        synchronized (client) {
            try {
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
            } catch (IOException ex) {
                client.setDisconnected();
            }
        }



    }
}
