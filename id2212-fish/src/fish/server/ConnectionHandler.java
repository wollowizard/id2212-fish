/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FileList;
import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.ListOfServer;
import fish.packets.ListeningServerPortNumber;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import fish.packets.Server;
import fish.packets.ServerStatistics;
import fish.server.entity.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
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

    /**
     *
     * @param fs
     * @param cd
     */
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


        while (running) {
            try {


                FishPacket fp = (FishPacket) in.readObject();
                System.out.println("FishPacket received!" + fp.getHeader().getType());
                if (fp.getHeader().getType() == PacketType.ADDFILE) {
                    FileList fl = (FileList) fp.getPayload();
                    ArrayList<FilenameAndAddress> listOfFishFilesToAdd = getListOfFishFilesToAdd(client, fl);
                    ArrayList<FilenameAndAddress> listOfFishFilesToRemove = getListOfFishFilesToRemove(client, fl);

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
                    sendListeningPortAck();
                } else if (fp.getHeader().getType() == PacketType.LISTOFSERVERS) {

                    System.out.println(fp.printSummary());
                    ListOfServer list=new ListOfServer(new ArrayList<Server>());
                    try {
                        list = new ListOfServer(fs.getListOfServers());
                    } catch (SQLException ex) {
                        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    FishPacket result = new FishPacket(new Header(PacketType.LISTOFSERVERS), list);
                    sendResult(result);
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
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            fs.clientDisconnected(client);
            try {
                closeConnection();
            } catch (IOException ex1) {
                System.out.println(ex1.getMessage());

            }
        }

    }

    private void sendListeningPortAck() {


        try {
            out.writeObject(new FishPacket(new Header(PacketType.LISTENINGSERVERPORTACK), new ListeningServerPortNumber(100)));

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

    /**
     *
     * @param client
     * @param fl
     * @return
     */
    public ArrayList<FilenameAndAddress> getListOfFishFilesToAdd(Client client, FileList fl) {
        ArrayList<String> filesToAdd = fl.getFilesToAdd();

        ArrayList<FilenameAndAddress> ret = new ArrayList<>();
        for (String s : filesToAdd) {
            FilenameAndAddress ff = new FilenameAndAddress(s, client.getRemoteIpAddress(), client.getListeningServerPort());
            ret.add(ff);
        }
        return ret;
    }

    /**
     *
     * @param client
     * @param fl
     * @return
     */
    public ArrayList<FilenameAndAddress> getListOfFishFilesToRemove(Client client, FileList fl) {

        ArrayList<String> filesToRemove = fl.getFilesToRemove();
        ArrayList<FilenameAndAddress> ret = new ArrayList<>();
        for (String s : filesToRemove) {
            FilenameAndAddress ff = new FilenameAndAddress(s, client.getRemoteIpAddress(), client.getListeningServerPort());
            ret.add(ff);
        }
        return ret;
    }
}
