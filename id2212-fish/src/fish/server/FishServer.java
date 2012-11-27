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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class FishServer {

    //public ArrayList<Client> connectedClients;
    //public ArrayList<FishFile> files;
    //Map<FishFile, Client> filesMap = new ConcurrentHashMap<>();
    List connectedClients = Collections.synchronizedList(new ArrayList());
    private String ip;
    private Integer port;
    public static final Integer supernodePort = 4321;
    private static String datasource = "fishdatabase";
    private static String user = "root";
    private static String passwd = "root";
    DataBaseManager dataBase;
    private int numClient = 0;
    String KeywordToSearch = "";
    private Server myServer = new Server(-1, "", -1);

    public void newClientConnected(Client c) {
        if (!connectedClients.contains(c)) {

            numClient++;
            this.connectedClients.add(c);
        }
    }

    public synchronized void clientDisconnected(Client client) {
        try {
            dataBase.deleteUser(client.getRemoteIpAddress(), client.getListeningServerPort());
            connectedClients.remove(client);
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void updateFilesOfClient(ArrayList<FilenameAndAddress> add, ArrayList<FilenameAndAddress> remove, Client client) {



        for (FilenameAndAddress s : add) {
            this.addFile(s);
        }
        for (FilenameAndAddress s : remove) {
            this.removeFile(s);
        }


    }

    public synchronized FishPacket search(Client c, String parameter) {
        Header header = null;

        ArrayList<FilenameAndAddress> results;
        try {
            results = dataBase.selectByFileName(parameter, c.getRemoteIpAddress(), c.getListeningServerPort());
            if (results.isEmpty()) {
                header = new Header(PacketType.FILENOTFOUND);
            } else {
                header = new Header(PacketType.FILEFOUND);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            header = new Header(PacketType.FILENOTFOUND);
            results = new ArrayList<>();
        }
        SearchResult sr = new SearchResult(results);
        FishPacket fp = new FishPacket(header, sr);
        return fp;





        /*
         this.KeywordToSearch = parameter;
         ArrayList<Map.Entry<FishFile, Client>> results = new ArrayList<>();
         ArrayList<String> word = new ArrayList<>();
         ArrayList<Integer> tmp = new ArrayList<>();
         StringTokenizer st = new StringTokenizer(parameter);
         while (st.hasMoreTokens()) {
         word.add(st.nextToken());
         }
         for (Map.Entry<FishFile, Client> entry : filesMap.entrySet()) {
         for (String it : word) {
         if (entry.getKey().getFilename().toLowerCase().contains(it.toLowerCase())
         && (!entry.getValue().equals(c)) && !results.contains(entry)) {
         results.add(entry);
         }
         }
         }
         Collections.sort(results, new SortByName());
         Header header = null;
         if (results.isEmpty()) {
         header = new Header(PacketType.FILENOTFOUND);
         } else {
         header = new Header(PacketType.FILEFOUND);
         }
         SearchResult sr = new SearchResult();
         for (Map.Entry<FishFile, Client> i : results) {
         FilenameAndAddress fr = new FilenameAndAddress(i.getKey().getFilename(), i.getValue().getNetResources().getSocket().getInetAddress().getHostAddress(), i.getValue().getListeningServerPort());
         sr.addFileResource(fr);
         }
         System.out.println("The server will send: " + sr.printSummary());
         FishPacket fp = new FishPacket(header, sr);
         return fp;
         */

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

    /*public class SortByName implements Comparator<FilenameAndAddress> {

     @Override
     public int compare(FilenameAndAddress t, FilenameAndAddress t1) {
     int s1 = Utils.getDifference(t.getKey().getFilename().toString(), KeywordToSearch);
     int s2 = Utils.getDifference(t1.getKey().getFilename().toString(), KeywordToSearch);
     return s1 - s2;
     }
     }*/
    public synchronized ArrayList<Server> getListOfServers() throws SQLException {
        return dataBase.getListOfServers();
    }

    void searchSuperNode() throws SQLException {
        Server sn = dataBase.getSuperNode();
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

    void supernodeCrashed(Server supernode) {
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
