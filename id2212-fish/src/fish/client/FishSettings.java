/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.client.controller.ClientController;
import fish.exceptions.NotDirectoryException;
import fish.exceptions.NotServerListFileException;
import fish.exceptions.WrongSettingException;
import fish.packets.Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class FishSettings {

    private Integer refreshInterval = 5000;
    private final static Integer MINREFRESHINTERVAL = 1000;
    private final static String MINVALUEERROR = "Minimum value: " + MINREFRESHINTERVAL.toString();
    private final static String INVALIDREFRESHVALUE = "Invalid value: you must digit a number greater than 1000";
    private final static String INVALIDFOLDER = "Invalid folder";
    private final static String INVALIDSERVERLISTFILE = "The list of servers is not a valid file";
    private final static String INVALIDPORT = "Invalid port number";
    private String folder = "C:\\Users\\alfredo\\Documents\\test\\temp1\\";
    private Integer port = 1234;
    private String ipAddress = "localhost";
    private Integer connectionTimeout = 1000;
    private Integer MINIMUMCONNECTIONTIMEOUT = 1000;
    private long DOWNLOADFOLDERREFRESHTIME = 10000; //10 SECONDS
    private long SHAREFOLDERREFRESHTIME = 2000; //2 SECONDS
    
    private String INVALIDCONNECTIONTIMEOUT = "Invalid Connection timeout. Minimum value: " + MINIMUMCONNECTIONTIMEOUT.toString();
    private String downloadFolder = "C:\\Users\\alfredo\\Documents\\test\\download1\\";
    private String serverlistfilepath = "C:\\Users\\alfredo\\Documents\\list.txt";
    private final static String STARTLINE = "####FISH SERVER LIST FILE####";
    private ArrayList<Server> currentServersList = new ArrayList<>();
    public Server currentServer;

    public FishSettings(ClientController aThis) {
    }

    public void setRefreshInterval(String s) throws NumberFormatException {
        try {
            Integer refrInt = Integer.parseInt(s);
            if (refrInt < MINREFRESHINTERVAL) {
                throw new NumberFormatException(MINVALUEERROR);
            }
            refreshInterval = refrInt;


        } catch (NumberFormatException e) {
            String msg = MINVALUEERROR;
            if (e.getMessage().compareTo(msg) != 0) {
                msg = INVALIDREFRESHVALUE;

            }
            throw new NumberFormatException(msg);
        }
    }
    
    

    public void setConnectionTimeout(String text) {
        try {
            Integer p = Integer.parseInt(text);
            if (p < MINIMUMCONNECTIONTIMEOUT) {
                throw new NumberFormatException(INVALIDCONNECTIONTIMEOUT);
            }
            connectionTimeout = p;


        } catch (NumberFormatException e) {

            throw new NumberFormatException(INVALIDCONNECTIONTIMEOUT);
        }
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setFolder(String text) throws NotDirectoryException {
        try {
            File f = new File(text);
            if (!f.isDirectory()) {
                throw new NotDirectoryException(INVALIDFOLDER);
            } else {
                this.folder = text;
                String separator = System.getProperty("file.separator");
                if (!folder.endsWith(separator)) {
                    folder += separator;
                }
            }
        } catch (Exception ex) {
            throw new NotDirectoryException(INVALIDFOLDER);
        }

    }

    public String getFolder() {
        return this.folder;
    }

    public void setDownloadFolder(String text) throws NotDirectoryException {
        try {
            File f = new File(text);
            if (!f.isDirectory()) {
                throw new NotDirectoryException(INVALIDFOLDER);
            } else {
                this.downloadFolder = text;
                String separator = System.getProperty("file.separator");
                if (!downloadFolder.endsWith(separator)) {
                    downloadFolder += separator;
                }
            }
        } catch (Exception ex) {
            throw new NotDirectoryException(INVALIDFOLDER);
        }

    }

    public String getDownloadFolder() {
        return this.downloadFolder;
    }

    public Integer getRefreshInterval() {
        return this.refreshInterval;
    }

    public void validateSettings() throws WrongSettingException {
        try {
            setRefreshInterval(refreshInterval.toString());
            setFolder(this.folder);
            setDownloadFolder(this.downloadFolder);
            setConnectionTimeout(connectionTimeout.toString());
            setServerListFile(this.serverlistfilepath);
        } catch (IOException ex) {
            throw new WrongSettingException(ex.getMessage());
        }
    }

    public void setServerListFile(String text) throws NotServerListFileException, FileNotFoundException, IOException {

        File f = new File(text);
        if (!f.isFile()) {
            throw new NotServerListFileException(INVALIDSERVERLISTFILE);
        } else {
            BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                if (line.compareTo(STARTLINE) != 0) {
                    throw new NotServerListFileException(INVALIDSERVERLISTFILE);
                }

                this.serverlistfilepath = text;
            } finally {
                br.close();
            }
        }
    }

    public void getServerFromFile() {
        ArrayList<Server> servers = new ArrayList<>();
        try {
            BufferedReader br = null;

            br = new BufferedReader(new FileReader(this.serverlistfilepath));
            String line = br.readLine();

            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    String[] split = line.split(":");
                    servers.add(new Server(split[0], Integer.parseInt(split[1])));
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(FishSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("servers: ------------------------------");

        this.currentServersList = servers;
    }

    public String getServerListFilePath() {
        return this.serverlistfilepath;
    }

    public void updateTextFileListOfServers(ArrayList<Server> servers) throws IOException {
        FileWriter fstream = new FileWriter(this.serverlistfilepath);
        BufferedWriter x = new BufferedWriter(fstream);
        x.write(STARTLINE);
        x.newLine();
        for (Server s : servers) {
            x.write(s.getAddress() + ":" + s.getPortForClients());
            x.newLine();
        }
        x.close();
    }

    public ArrayList<Server> getCurrentServersList() {
        return this.currentServersList;
    }

    public long getSharedFolderRefreshTime() {
        return SHAREFOLDERREFRESHTIME;
    }

    public long getDownloadFolderRefreshTime() {
        return DOWNLOADFOLDERREFRESHTIME;
    }
}
