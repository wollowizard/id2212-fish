/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FileListPacket;
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

    private FileListPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;

    public Communicator(FileListPacket p, ObjectInputStream in, ObjectOutputStream out, Client client) {

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
                System.out.println(this.packet.printSummary() + "\n\n");

                out.toString();

                out.writeObject(this.packet);
                out.flush();
                out.reset();//important!!!!!!!! we always send the same obj, but fields are changed

                client.filesToAdd.clear();
                client.filesToRemove.clear();
                
            }

        } catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
