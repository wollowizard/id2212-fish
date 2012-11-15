/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

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

    String ip;
    Integer port;
    Client client;

    public Connector(Client c) {

        this.client = c;
        this.port = c.getSettings().getPort();
        this.ip = c.getSettings().getIpAddress();

    }

    @Override
    public void run() {

        try {
            synchronized (client) {
                System.out.println("\n\nCONNECTOR!!\n\n");
                Socket sock = new Socket();
                sock.connect(new InetSocketAddress(ip, port), client.getSettings().getConnectionTimeout());
                
                client.setSocket(sock);
                ObjectOutputStream objOut = new ObjectOutputStream(sock.getOutputStream());

                ObjectInputStream objIn = new ObjectInputStream(sock.getInputStream());

                client.setInStream(objIn);
                client.setOutStream(objOut);
                client.setConnected();
                client.startReceiverThread();



                objIn.toString();
                objOut.toString();


                client.submitInitialFileList();
                //Create a file chooser

                client.startStatisticsThread();

            }
        } catch (IOException ex) {
            
            client.setErrorMessage(ex.getMessage());
            client.notifyObservers(EventEnum.NEWERRORMESSAGE);

        }
    }
}
