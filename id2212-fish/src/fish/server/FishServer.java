/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.database.DataBaseManager;
import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import fish.packets.SearchResult;
import fish.packets.ServerStatistics;
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
class FishServer {

    //public ArrayList<Client> connectedClients;
    //public ArrayList<FishFile> files;
    //Map<FishFile, Client> filesMap = new ConcurrentHashMap<>();
    List connectedClients = Collections.synchronizedList(new ArrayList());
    private static String datasource = "fishdatabase";
    private static String user = "root";
    private static String passwd = "root";
    DataBaseManager dataBase;
    private int numClient = 0;
    String KeywordToSearch = "";

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
            ss = new ServerStatistics(connectedClients.size(), dataBase.getFileCount());
        } catch (SQLException ex) {
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
}
