/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FileListPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel
 */
public class ConnectionHandler extends Thread {

    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    private boolean running;
    private Client client;
    private FishServer fs;

    public ConnectionHandler(FishServer fs, Client cd) {
        this.client = cd;
        this.fs = fs;
        this.socket = cd.getNetResources().getSocket();
        this.in = cd.getNetResources().getInStream();
        this.out = cd.getNetResources().getOutStream();
        this.running = true;

    }

    @Override
    public void run() {
        fs.clients.add(client);
        System.out.println("Summary:\n");
        System.out.println(fs.printSummary());

        while (running) {


            try {


                FileListPacket fp = (FileListPacket) in.readObject();

                System.out.println((new Date()).toString() + " Received packet: ");
                System.out.println(fp.printSummary());

                ArrayList<String> toAdd = fp.getFilesToAdd();
                ArrayList<String> toRemove = fp.getFilesToRemove();

                for (String s : toAdd) {
                    client.addFile(s);
                }

                for (String s : toRemove) {
                    client.removeFile(s);
                }

                System.out.println("\n\n" + client.printSummary() + "\n\n");

            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

    }
}
