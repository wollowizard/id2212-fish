/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.server.entity.Client;
import fish.server.entity.ClientNetworkResources;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel
 */
public class Main {

    public static void main(String[] args) throws IOException {

        String port = "1237";

        boolean listening = true;
        ServerSocket serverSocket = null;
        final FishServer fs = new FishServer();
        fs.ConnectToDataBase();
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + port);
            System.exit(1);
        }
        InetAddress thisIp = InetAddress.getLocalHost();
        fs.setIp(thisIp.getHostAddress().toString());
        fs.setPort(Integer.parseInt(port));

        try {
            fs.searchSuperNode();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

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