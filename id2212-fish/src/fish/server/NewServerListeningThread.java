/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.server.database.DataBaseManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class NewServerListeningThread extends Thread {

    private Integer port;
    private DataBaseManager db;

    /**
     *
     * @param listport
     * @param d
     */
    public NewServerListeningThread(Integer listport, DataBaseManager d) {
        this.port = listport;
        db = d;

    }

    @Override
    public void run() {
        ServerSocket providerSocket = null;
        try {

            providerSocket = new ServerSocket(port);
            boolean running = true;

            while (running) {
                System.out.println("Supernode accepting connection");
                Socket clientSocket = providerSocket.accept();
                System.out.println("Supernode accepted connection ");
                
                ServerCrashDetectorThread thr = new ServerCrashDetectorThread(clientSocket, db);
                thr.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(NewServerListeningThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}