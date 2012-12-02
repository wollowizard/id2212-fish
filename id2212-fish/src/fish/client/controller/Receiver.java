/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.ListOfServer;
import fish.packets.SearchResult;
import fish.packets.ServerStatistics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The thread that receives packets
 * @author alfredo
 */
public class Receiver extends Thread {

    //private FishPacket packet;
    private ObjectInputStream in;
    private ClientController client;
    private Boolean running = true;

    /**
     * Constructor
     * @param in the input stream to receive from
     * @param client the client controller
     */
    public Receiver(ObjectInputStream in, ClientController client) {


        this.in = in;
        this.client = client;
    }

    @Override
    public void run() {


        try {


            System.out.println("\n\nRECEIVER!!\n\n");
            while (running) {
                FishPacket fp = (FishPacket) in.readObject();
                synchronized (client) {
                    this.manageResponse(fp);

                }

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.client.setDisconnected();
        }
    }

    private void manageResponse(FishPacket received) throws IOException {
        switch (received.getHeader().getType()) {
            case FILENOTFOUND:
                System.out.println("FILE NOT FOUND!!!!!");
                this.client.setLastresult(new ArrayList<FilenameAndAddress>());
                break;
            case FILEFOUND:
                System.out.print("FILE FOUND !!!!! ");
                SearchResult results = (SearchResult) received.getPayload();
                this.client.setLastresult(results.getFileNamesandAddresses());
                break;

            case STATISTICS:
                ServerStatistics ss = (ServerStatistics) received.getPayload();
                this.client.setStatistics(ss.getNumClients(), ss.getNumFiles());
                break;
            case FILENOLONGERAVAILABLE:
                System.out.println("FILE NO LONG AVAILABLE RESPONSE!!!");
                break;
            case LISTOFSERVERS:
                ListOfServer ls=(ListOfServer) received.getPayload();
                this.client.newListOfServerReceived(ls.servers);
                System.out.println("received list of servers\n\n" + ls.printSummary());
                break;
            default:
                break;

        }
    }
}
