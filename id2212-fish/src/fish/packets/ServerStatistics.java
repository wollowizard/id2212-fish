/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 *
 * @author Marcel
 */
public class ServerStatistics extends Payload implements Serializable {

    private int numClients;
    private int numFiles;

    /**
     *
     * @param nClient
     * @param nFiles
     */
    public ServerStatistics(int nClient, int nFiles) {
        this.numClients = nClient;
        this.numFiles = nFiles;
    }

    /**
     *
     * @return
     */
    public int getNumClients() {
        return this.numClients;
    }

    /**
     *
     * @return
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
