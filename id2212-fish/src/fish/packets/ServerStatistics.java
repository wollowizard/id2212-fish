/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * This payload is used to communicate statistics to the client
 * @author Marcel
 */
public class ServerStatistics extends Payload implements Serializable {

    private int numClients;
    private int numFiles;

    /**
     *Constructor
     * @param nClient the number of clients currently connected
     * @param nFiles the number of files in the db now
     */
    public ServerStatistics(int nClient, int nFiles) {
        this.numClients = nClient;
        this.numFiles = nFiles;
    }

    /**
     * gets the number of clients currently connected
     * @return the number of clients currently connected
     */
    public int getNumClients() {
        return this.numClients;
    }

    /**
     * gets the number of files
     * @return the number of files
     */
    public int getNumFiles() {
        return this.numFiles;
    }

    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        return "";
        //return "Server statistics\nNumber of Clients = " + getNumClients() + "\nNumber of Files = " + getNumFiles();
    }
}
