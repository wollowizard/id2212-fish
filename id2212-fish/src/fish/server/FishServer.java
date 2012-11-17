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

    //public ArrayList<Client> clients;
    //public ArrayList<FishFile> files;
    
    Map<FishFile,Client> filesMap=new ConcurrentHashMap<>();
    
    List clients = Collections.synchronizedList(new ArrayList()); 
    private static String datasource = "fishdatabase";
    private static String user = "root";
    private static String passwd = "root";
    DataBaseManager dataBase;
    private int numClient = 0;        
    String KeywordToSearch="";
  
    public void newClientConnected(Client c) {
        if (!clients.contains(c)) {
            c.setClientName("c"+numClient);
            numClient++;
            this.clients.add(c);
        }
    }

    public synchronized void clientDisconnected(Client client) {
        ArrayList<Map.Entry<FishFile,Client>> toberemoved=new ArrayList<>();
        try {
            dataBase.deleteClient(filesMap.get(client).getClientName());
        } catch (SQLException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Map.Entry<FishFile,Client> entry : filesMap.entrySet()){
            if(entry.getValue().equals(client)){
                toberemoved.add(entry);
            }
        }

        for (Map.Entry<FishFile, Client> f : toberemoved) {
            filesMap.remove(f.getKey());
        }

        client.clearFiles();
        clients.remove(client);
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

    public static int getDifference(String a, String b) {
        // Minimize the amount of storage needed:
        if (a.length() > b.length()) {
            // Swap:
            String x = a;
            a = b;
            b = x;
        }

        // Store only two rows of the matrix, instead of a big one
        int[] mat1 = new int[a.length() + 1];
        int[] mat2 = new int[a.length() + 1];

        int i;
        int j;

        for (i = 1; i <= a.length(); i++) {
            mat1[i] = i;
        }

        mat2[0] = 1;

        for (j = 1; j <= b.length(); j++) {
            for (i = 1; i <= a.length(); i++) {
                int c = (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);

                mat2[i] =
                        Math.min(mat1[i - 1] + c,
                        Math.min(mat1[i] + 1, mat2[i - 1] + 1));
            }

            // Swap:
            int[] x = mat1;
            mat1 = mat2;
            mat2 = x;

            mat2[0] = mat1[0] + 1;
        }

        // It's row #1 because we swap rows at the end of each outer loop,
        // as we are to return the last number on the lowest row
        return mat1[a.length()];
    }

    public synchronized FishPacket search(Client c, String parameter) {
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

    }

    private synchronized boolean addFile(Client c, FishFile file) {
        boolean found=false;
        try {
            dataBase.insert(file.getFilename(), c.getClientName(), c.getNetResources().getSocket().getRemoteSocketAddress().toString());
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Map.Entry<FishFile,Client> entry : filesMap.entrySet()){
            if(found){
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
        this.clients.remove(c);
        
    }

    private synchronized  void removeFile(Client c, FishFile file) {
        try {
            dataBase.delete(file.getFilename(), c.getClientName());
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.filesMap.remove(file);

    }

    public synchronized String printSummary() {
        String res = "";
        for (Object o : clients) {
            Client c = (Client) o;
            res += c.printSummary() + "\n\n\n";
        }
        return res;

    }

    public synchronized FishPacket getStatistics(Client client) {
        return new FishPacket(new Header(PacketType.STATISTICS),
                new ServerStatistics(clients.size(), filesMap.size()));
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
    
    public class SortByName implements Comparator<Map.Entry<FishFile,Client>> {

        @Override
        public int compare(Entry<FishFile, Client> t, Entry<FishFile, Client> t1) {
            int s1 = getDifference(t.getKey().getFilename().toString(), KeywordToSearch);
            int s2 = getDifference(t1.getKey().getFilename().toString(), KeywordToSearch);
            return s1 - s2;
        }
    }
    

}
