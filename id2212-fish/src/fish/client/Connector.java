/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Connector extends Thread {

    String ip;
    Integer port;
    Client client;

    public Connector(String ip, Integer port, Client c) {
        this.ip = ip;
        this.port = port;
        this.client = c;

    }

    @Override
    public void run() {
        synchronized (this.client) {

            try {

                Socket sock = new Socket(ip, port);
                client.setSocket(sock);
                ObjectOutputStream objOut = new ObjectOutputStream(sock.getOutputStream());

                ObjectInputStream objIn = new ObjectInputStream(sock.getInputStream());

                client.setInStream(objIn);
                client.setOutStream(objOut);
                objIn.toString();
                objOut.toString();





            } catch (UnknownHostException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
