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
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
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
        dataBase.deleteClient(client.getClientName());
        dataBase.deleteFilesOf(client.getClientName());
        connectedClients.remove(client);


        /*
         ArrayList<Map.Entry<FishFile, Client>> toberemoved = new ArrayList<>();
         try {
         dataBase.deleteClient(client.getClientName());
         } catch (SQLException ex) {
         Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
         }
         for (Map.Entry<FishFile, Client> entry : filesMap.entrySet()) {
         if (entry.getValue().equals(client)) {
         toberemoved.add(entry);
         }
         }

         for (Map.Entry<FishFile, Client> f : toberemoved) {
         filesMap.remove(f.getKey());
         }

         client.clearFiles();
         connectedClients.remove(client);
         */
    }

    public synchronized void updateFilesOfClient(ArrayList<FishFile> add, ArrayList<FishFile> remove, Client client) {

        for (FishFile s : add) {

            if (addFile(client, s)) {
                client.addFile(s);
            }
        }

        for (FishFile s : remove) {
            client.removeFile(s);
            removeFile(client, s);
        }

    }

    public synchronized FishPacket search(Client c, String parameter) {
        Header header = null;
        ArrayList<FishFile> searchFiles = dataBase.searchFiles(c.getClientName(), parameter);
        ArrayList<FilenameAndAddress> res= new ArrayList<>();
        for(FishFile ff : searchFiles){
            res.add(new FilenameAndAddress(user, user, numClient));
        }




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

    private synchronized boolean addFile(Client c, FishFile file) {
        boolean found = false;
        try {
            dataBase.insert(file.getFilename(), c.getClientName(), c.getNetResources().getSocket().getRemoteSocketAddress().toString());
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Map.Entry<FishFile, Client> entry : filesMap.entrySet()) {
            if (found) {
                break;
            }
            FishFile fileInMap = entry.getKey();
            if ((fileInMap.getFilename().compareTo(file.getFilename()) == 0)
                    && fileInMap.getOwner().equals(c)) {
                found = true;
            }
        }

        if (!found) {
            this.filesMap.put(file, c);
        }
        return !found;

    }

    private synchronized void removeClient(Client c) {
        try {
            dataBase.deleteClient(c.getClientName());
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.connectedClients.remove(c);

    }

    private synchronized void removeFile(Client c, FishFile file) {
        try {
            dataBase.delete(file.getFilename(), c.getClientName());
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.filesMap.remove(file);

    }

    public synchronized String printSummary() {
        String res = "";
        for (Object o : connectedClients) {
            Client c = (Client) o;
            res += c.printSummary() + "\n\n\n";
        }
        return res;

    }

    public synchronized FishPacket getStatistics(Client client) {
        return new FishPacket(new Header(PacketType.STATISTICS),
                new ServerStatistics(connectedClients.size(), filesMap.size()));
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

    public class SortByName implements Comparator<Map.Entry<FishFile, Client>> {

        @Override
        public int compare(Entry<FishFile, Client> t, Entry<FishFile, Client> t1) {
            int s1 = Utils.getDifference(t.getKey().getFilename().toString(), KeywordToSearch);
            int s2 = Utils.getDifference(t1.getKey().getFilename().toString(), KeywordToSearch);
            return s1 - s2;
        }
    }
}
