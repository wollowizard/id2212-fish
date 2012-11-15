/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.SearchResult;
import fish.packets.ServerStatistics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class Receiver extends Thread {

    //private FishPacket packet;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;
    private Boolean running = true;

    public Receiver(ObjectInputStream in, ObjectOutputStream out, Client client) {

        //this.packet = p;
        this.in = in;
        this.out = out;
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


    private void manageResponse(FishPacket received) {
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
            case DOWNLOAD:
                break;
            case STATISTICS:
                ServerStatistics ss = (ServerStatistics) received.getPayload();
                this.client.setStatistics(ss.getNumClients(),ss.getNumFiles());
                break;
            default:
                break;

        }
    }
}
