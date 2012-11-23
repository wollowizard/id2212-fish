/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FileList;
import fish.packets.FishPacket;
import fish.packets.ListeningServerPortNumber;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import fish.packets.ServerStatistics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
                System.out.println("FishPacket recived!" + fp.getHeader().getType());
                if (fp.getHeader().getType() == PacketType.ADDFILE) {
                    FileList fl = (FileList) fp.getPayload();
                    ArrayList<FishFile> listOfFishFilesToAdd = getListOfFishFilesToAdd(client,fl);
                    ArrayList<FishFile> listOfFishFilesToRemove = getListOfFishFilesToRemove(client,fl);

                    fs.updateFilesOfClient(listOfFishFilesToAdd, listOfFishFilesToRemove, client);
                    
                } else if (fp.getHeader().getType() == PacketType.SEARCH) {

                    ParameterToSearch par = (ParameterToSearch) fp.getPayload();
                    System.out.println(fp.getPayload().printSummary());
                    FishPacket search = fs.search(client, par.getParameter());
                    sendResult(search);

                } else if (fp.getHeader().getType() == PacketType.STATISTICS) {
                    ServerStatistics sts = (ServerStatistics) fp.getPayload();
                    FishPacket result = fs.getStatistics(client);
                    sendResult(result);
                } else if (fp.getHeader().getType() == PacketType.LISTENINGSERVERPORT) {

                    ListeningServerPortNumber pn = (ListeningServerPortNumber) fp.getPayload();
                    System.out.println("Received: " + pn.printSummary());
                    Integer port = pn.port;
                    client.setListeningServerPort(port);
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

    private void sendResult(FishPacket response) {


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

    public ArrayList<FishFile> getListOfFishFilesToAdd(Client client, FileList fl) {
        ArrayList<String> filesToAdd=fl.getFilesToAdd();
        
        ArrayList<FishFile> ret = new ArrayList<>();
        for (String fnwh : filesToAdd) {
            FishFile ff = new FishFile(fnwh, client);
            ret.add(ff);
        }
        return ret;
    }

    public ArrayList<FishFile> getListOfFishFilesToRemove(Client client,FileList fl) {
        
        ArrayList<String> filesToRemove=fl.getFilesToRemove();
        ArrayList<FishFile> ret = new ArrayList<>();
        for (String fnwh : filesToRemove) {
            FishFile ff = new FishFile(fnwh, client);
            ret.add(ff);
        }
        return ret;
    }
}
