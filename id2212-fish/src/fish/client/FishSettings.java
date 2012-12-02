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
    private String folder = ".\\sharedFolder";
  
    private String ipAddress = "localhost";
    private Integer connectionTimeout = 1000;
    private Integer MINIMUMCONNECTIONTIMEOUT = 1000;
    private long DOWNLOADFOLDERREFRESHTIME = 5000; //5 SECONDS
    private long SHAREFOLDERREFRESHTIME = 2000; //2 SECONDS
    private String INVALIDCONNECTIONTIMEOUT = "Invalid Connection timeout. Minimum value: " + MINIMUMCONNECTIONTIMEOUT.toString();
    private String downloadFolder = ".\\downloads";
    private String serverlistfilepath = ".\\list.txt";
    private final static String STARTLINE = "####FISH SERVER LIST FILE####";
    private ArrayList<Server> currentServersList = new ArrayList<>();
    /**
     * The current server the client is connected to
     */
    public Server currentConnectedServer;
    /**
     * The current server the client is connecting to
     */
    public Server currentConnectingServer;
    /**
     * The path of the folder containing the files to share
     */
    public final static String SHARED_FOLDER = "SharedFolder";
    /**
     * The path of the folder where to download the files
     */
    public final static String DOWNLOAD_FOLDER = "DownloadFolder";
    /**
     *  The path of the folder where the list of servers is
     */
    public final static String SERVER_FOLDER = "ServerFolder";

    /**
     *
     * The constructor.
     */
    public FishSettings() {
    }

    /**
     *Sets the refresh interval
     * @param s The interval in ms (String)
     * @throws NumberFormatException if the string cannot be parsed to an Integer
     */
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
    
    /**
     * 
     * @return the refresh interval
     */
    public Integer getRefreshInterval() {
        return this.refreshInterval;
    }


    /**
     * Sets the timeout for the connection
     * @param text the timeout (String)
     */
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

    /**
     *  Returns the timeout for the connection
     * @return the timeout of the connection
     */
    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    /**
     * Returns the ip address of the client (used to communicate to the server)
     * @return the ip address as a string
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * Sets the folder with the files to share
     * @param text the folder that contains the files to share
     * @throws NotDirectoryException if it is not a folder
     */
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

    /**
     * Returns the folder with the files to share. An absolute path
     * @return the path of the folder with the files to share
     */
    public String getFolder() {
        return this.folder;
    }

    /**
     * Sets the folder where to download the files
     * @param text the absolute path 
     * @throws NotDirectoryException If not a valid folder is given
     */
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

    /**
     * returns the absolute path of the folder where the files are downloaded
     * @return the folder where the downloads are put
     */
    public String getDownloadFolder() {
        return this.downloadFolder;
    }

    
    /**
     *
     * @throws WrongSettingException
     */
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

    /**
     *
     * @param text
     * @throws NotServerListFileException
     * @throws FileNotFoundException
     * @throws IOException
     */
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

    /**
     * it updates the currentServersList variable which contains a list of the available servers, reading from a local file
     */
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
        

        this.currentServersList = servers;
    }

    /**
     * it returns the absolute path of the file that contains the list of servers
     * @return the path of the file that contains the list of servers
     */
    public String getServerListFilePath() {
        return this.serverlistfilepath;
    }

    /**
     * It writes on the file a list of servers
     * @param servers the list of servers
     * @throws IOException if the write operation cannot be done
     */
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

    /**
     * It gives a list of servers (taking it from memory)
     * @return the current list of servers
     */
    public ArrayList<Server> getCurrentServersList() {
        return this.currentServersList;
    }

    /**
     * It returns the refresh time of the folder containing the files to share (there is a periodical check of the folder)
     * @return the refresh time of the shared folder 
     */
    public long getSharedFolderRefreshTime() {
        return SHAREFOLDERREFRESHTIME;
    }

    /**
     *It returns the refresh time of the folder containing the downloaded files (there is a periodical check of the folder)
     * @return the refresh time of the folder  with the downloads
     */
    public long getDownloadFolderRefreshTime() {
        return DOWNLOADFOLDERREFRESHTIME;
    }
}
