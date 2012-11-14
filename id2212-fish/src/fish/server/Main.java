/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Marcel
 */
public class Main {

    public static void main(String[] args) throws IOException {
                
        boolean listening = true;
        ServerSocket serverSocket = null;

        final FishServer fs = new FishServer();

        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 1234.");
            System.exit(1);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame(fs).setVisible(true);
            }
        });
        
        while (listening) {

            Socket clientSocket = serverSocket.accept();
            
            Client cd;
            cd = new Client(new ClientNetworkResources(clientSocket));
            fs.newClientConnected(cd);
            
            (new ConnectionHandler(fs, cd)).start();

        }
        serverSocket.close();
        
        
    }
}

