/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FileList;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.Payload;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel
 */
public class ConnectionHandler extends Thread {

    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    private boolean running;
    private Client client;
    private FishServer fs;

    public ConnectionHandler(FishServer fs, Client cd) {
        this.client = cd;
        this.fs = fs;
        this.socket = cd.getNetResources().getSocket();
        this.in = cd.getNetResources().getInStream();
        this.out = cd.getNetResources().getOutStream();
        this.running = true;

    }

    @Override
    public void run() {

        System.out.println("Summary:\n");
        System.out.println(fs.printSummary());
        
        
        
        while (running) {


            try {


                FishPacket fp = (FishPacket) in.readObject();

                if (fp.getHeader().getType() == PacketType.ADDFILE) {
                    FileList fl = (FileList) fp.getPayload();
                    fs.addFilesOfClient(fl, client);
                    
                    

                }
                else if(fp.getHeader().getType() == PacketType.SEARCH){
                    
                    ParameterToSearch par=(ParameterToSearch) fp.getPayload();
                    System.out.println(fp.getPayload().printSummary());
                    FishPacket search = fs.search(client, par.getParameter());
                    sendSearchResult(search);
                
                }

            } catch (IOException ex) {

                running = false;
                System.out.println("Client " + client.getNetResources().getSocket().getInetAddress() + "disconnected");
                fs.clientDisconnected(client);


            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //client disconnects
        try {
            closeConnection();
        } catch (IOException e) {
            System.out.println("Error while closing the connection");
            e.printStackTrace();
        }

    }

    private void closeConnection() throws IOException {
        out.close();
        in.close();
        socket.close();

    }

    private void sendSearchResult(FishPacket response) {
        
        
        try {
            out.writeObject(response);
            
            
        } catch (IOException ex) {
            //client connection was not ok
            System.out.println(ex.getMessage());
            fs.clientDisconnected(client);
            try {
                closeConnection();
            } catch (IOException ex1) {
                System.out.println(ex1.getMessage());
                
            }
        }

    }
}
