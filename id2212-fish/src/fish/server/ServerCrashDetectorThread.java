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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * a thread with an active connection to all the other servers (started by the SN).
 * 
 * @author alfredo
 */
public class ServerCrashDetectorThread extends Thread {

    private Socket socket;
    private DataBaseManager db;

    /**
     *
     * @param s
     * @param d
     */
    public ServerCrashDetectorThread(Socket s, DataBaseManager d) {
        this.socket = s;
        this.db = d;

    }

    @Override
    public void run() {
        System.out.println("Supernode crash detector started");
        ObjectInputStream in = null;
        Server serverWithId = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("waiting for data");
            
            Server server = (Server) in.readObject();
            
            System.out.println("data came");
            try {
                serverWithId = db.addServer(server.getAddress(), server.getPortForClients());
                out.writeObject(serverWithId);
                
            } catch (SQLException ex) {
                Logger.getLogger(ServerCrashDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            boolean running = true;
            while (running) {
                in.readObject();
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerCrashDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("server crash detected. About to remove" + serverWithId.getPortForClients());
            if (serverWithId != null) {
                System.out.println("server crash detected. About to remove" + serverWithId.getPortForClients());
                try {
                    db.removeServer(serverWithId);
                } catch (SQLException ex1) {
                    Logger.getLogger(ServerCrashDetectorThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }


    }
}
