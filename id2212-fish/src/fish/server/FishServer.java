/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import fish.packets.SearchResult;
import fish.packets.Server;
import fish.packets.ServerStatistics;
import fish.server.database.DataBaseManager;
import fish.server.entity.Client;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The most important class on the server. it handles all the server side logic
 * @author alfredo
 */
public class FishServer {

    //public ArrayList<Client> connectedClients;
    //public ArrayList<FishFile> files;
    //Map<FishFile, Client> filesMap = new ConcurrentHashMap<>();
    List connectedClients = Collections.synchronizedList(new ArrayList());
    private String ip;
    private Integer port;
    /**
     * the port the supernode is accepting connections on (from other servers). The value is 4321
     */
    public static final Integer supernodePort = 4321;
    private static String datasource = "fishdatabase";
    private static String user = "root";
    private static String passwd = "root";
    DataBaseManager dataBase;
    
    String KeywordToSearch = "";
    private Server myServer = new Server(-1, "", -1);

    
    public FishServer(String dbname, String dbuser, String dbpassword){
        datasource=dbname;
        user=dbuser;
        passwd=dbpassword;
    }
    /**
     * when a new client is connected, it is added to a list of connected 
     * clients
     * @param c
     */
    public void newClientConnected(Client c) {
        if (!connectedClients.contains(c)) {

            this.connectedClients.add(c);
        }
    }

    /**
     * when a client disconnects (or crashes), its files are deleted from the db 
     * and the client is removed from the list of connected clients
     * @param client
     */
    public synchronized void clientDisconnected(Client client) {
        try {
            dataBase.deleteClient(client.getRemoteIpAddress(), client.getListeningServerPort());
            connectedClients.remove(client);
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Adds and removes files of a client
     * @param add the files to add
     * @param remove the files to remove
     * @param client the client performing the operation
     */
    public synchronized void updateFilesOfClient(ArrayList<FilenameAndAddress> add, ArrayList<FilenameAndAddress> remove, Client client) {



        for (FilenameAndAddress s : add) {
            this.addFile(s);
        }
        for (FilenameAndAddress s : remove) {
            this.removeFile(s);
        }


    }

    /**
     * Searches for a file on the db given a parameter. it supports multiple keywords
     * with multiple searches. multiple results are displayed once (the results
     * are merged without repetitions)
     * @param c the client that is searching the file
     * @param parameter the parameter to search
     * @return a fishPacket containing the search result
     */
    public synchronized FishPacket search(Client c, String parameter) {
        Header header = null;

        HashSet <FilenameAndAddress> results=new HashSet<>();
        try {
            String[] split = parameter.split(" ");
            for (String str : split) {
                HashSet <FilenameAndAddress> tmp;
                tmp = dataBase.selectByFileName(str, c.getRemoteIpAddress(), c.getListeningServerPort());
                results.addAll(tmp);
            }


            if (results.isEmpty()) {
                header = new Header(PacketType.FILENOTFOUND);
            } else {
                header = new Header(PacketType.FILEFOUND);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            header = new Header(PacketType.FILENOTFOUND);
            results = new HashSet<>();
        }
        
        ArrayList<FilenameAndAddress> array=new ArrayList<>();
        array.addAll(results);
        
        SearchResult sr = new SearchResult(array);
        
        FishPacket fp = new FishPacket(header, sr);
        return fp;


    }

    private synchronized void addFile(FilenameAndAddress file) {
        try {
            dataBase.insertFile(file.getFilename(), file.getAddress(), file.getPort());
        } catch (Exception ex) {
        }
    }

    private synchronized void removeFile(FilenameAndAddress file) {
        try {
            dataBase.deleteFile(file.getFilename(), file.getAddress(), file.getPort());
        } catch (Exception ex) {
        }
    }

    /**
     * Gets a fishpacket with the statistics
     * @param client the client requesting the statistics
     * @return a fishpacket with the statistics
     */
    public synchronized FishPacket getStatistics(Client client) {

        ServerStatistics ss;
        try {
            ss = new ServerStatistics(dataBase.getClientsCount(), dataBase.getFileCount());
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            ss = new ServerStatistics(-1, -1);
        }

        return new FishPacket(new Header(PacketType.STATISTICS), ss);

    }

    /**
     * connects to the db
     */
    public void ConnectToDataBase() {
        dataBase = new DataBaseManager(user, passwd, datasource);

        try {
            dataBase.connectDatabase(user, passwd);
            try {
                dataBase.createTable();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    /**
     * gets the list of active servers
     * @return the list of active servers
     * @throws SQLException if something goes wrong in the db interaction
     */
    public synchronized ArrayList<Server> getListOfServers() throws SQLException {
        return dataBase.getListOfServers();
    }

    
    /**
     * searches the SN in the db. if no other servers found, it becomes the 
     * supernode, starting a NewServerListeningThread. if found, connects to the SN 
     * @throws SQLException 
     */
    
    public void searchSuperNode() throws SQLException {
        Server sn = dataBase.getSuperNode();
        if (sn != null && sn.getAddress().equals(ip) && sn.getPortForClients().equals(port)) {
            //clean if I am the first server to start
            dataBase.truncateServerTable();
            sn = null;
        }
        if (sn == null) {
            //no supernode, I become the supernode!
            System.out.println("supernode not found");

            dataBase.addSupernode(ip, port);

            NewServerListeningThread nslt = new NewServerListeningThread(FishServer.supernodePort, dataBase);
            nslt.start();
        } else {
            //the supernode exists;
            System.out.println("supernode gotten: " + sn.getId());
            ConnectionToSuperNodeThread th = new ConnectionToSuperNodeThread(myServer, sn, this);
            th.start();
        }

    }

    /**
     * when a remote SN crashes, this method is called to remove it from the db 
     * and start the super node election phase
     * @param supernode 
     */
    public void supernodeCrashed(Server supernode) {
        try {
            dataBase.removeServer(supernode);
            this.SuperNodeElection();

        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SuperNodeElection() throws SQLException {

        boolean IAMTHESUPERNODE = false;
        while (dataBase.getSuperNode() == null && !IAMTHESUPERNODE) {

            Integer minId = dataBase.getMinId();
            if (dataBase.getServer(ip, port).getId() == minId) {
                dataBase.promoteSupernode(ip, port);
                IAMTHESUPERNODE = true;
                NewServerListeningThread nslt = new NewServerListeningThread(FishServer.supernodePort, dataBase);
                nslt.start();
            }
        }
        if (!IAMTHESUPERNODE) {
            ConnectionToSuperNodeThread th = new ConnectionToSuperNodeThread(myServer, dataBase.getSuperNode(), this);
            th.start();
        }
    }

    void setIp(String toString) {
        this.ip = toString;
        myServer.setAddress(ip);
    }

    void setPort(int parseInt) {
        this.port = parseInt;
        myServer.setPort(port);
    }

    void refreshMyServer(Server me) {
        myServer = me;
    }
}
