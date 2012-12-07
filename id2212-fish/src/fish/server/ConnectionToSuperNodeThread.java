/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.Server;
import fish.server.database.DataBaseManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this thread makes a connection to the supernode thread and then reads. If
 * the read returns, it means the supernode crashed and so the supernode election
 * procedure is started
 * @author alfredo
 */
class ConnectionToSuperNodeThread extends Thread {

    private final Server myself;
    private final Server sn;
    private FishServer fs;
    private DataBaseManager db;

    ConnectionToSuperNodeThread(Server me, Server supernode, FishServer f) {
        this.myself = me;
        this.sn = supernode;
        fs = f;


    }

    @Override
    public void run() {
        try {
            System.out.println("Connection to supernode thread started");
            Socket requestSocket = new Socket(sn.getAddress(), FishServer.supernodePort);
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

            System.out.println("about to write data");
            out.writeObject(new Server(myself.getAddress(), myself.getPortForClients()));
            out.flush();
            Server me=(Server)in.readObject();
            fs.refreshMyServer(me);
            
                        
            System.out.println("data written, about to read");

            in.read();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionToSuperNodeThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerCrashDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //if here, read returned. start sn election
            System.out.println("I have detected that supernode crashed");
            fs.supernodeCrashed(this.sn);

        }

    }
}
