/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import fish.client.EventEnum;
import fish.packets.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author alfredo
 */
public class Connector extends Thread {

    private Server server;
    ClientController client;

    public Connector(ClientController c, Server s) {

        this.client = c;
        server = s;

    }

    @Override
    public void run() {

        try {
            synchronized (client) {
                System.out.println("\n\nCONNECTOR trying to connect to " + server.getAddress() + ":" + server.getPortForClients() + "\n\n");
                client.getSettings().currentConnectingServer = server;
                ViewNotifier.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        client.notifyObservers(EventEnum.CONNECTINGTO);
                    }
                });

                Socket sock = new Socket();
                sock.connect(new InetSocketAddress(server.getAddress(), server.getPortForClients()), client.getSettings().getConnectionTimeout());

                client.getNetData().setSocket(sock);
                client.getSettings().currentConnectedServer = server;
                ObjectOutputStream objOut = new ObjectOutputStream(sock.getOutputStream());

                ObjectInputStream objIn = new ObjectInputStream(sock.getInputStream());


                client.getNetData().setInStream(objIn);
                client.getNetData().setOutStream(objOut);

                client.startConnection();
                objIn.toString();
                objOut.toString();



            }
        } catch (IOException ex) {

            client.setErrorMessage(ex.getMessage());
            ViewNotifier.invokeLater(new Runnable() {
                @Override
                public void run() {
                    client.notifyObservers(EventEnum.CONNECTIONFAILED);
                    client.tryNextServer();

                }
            });


        }
    }
}
