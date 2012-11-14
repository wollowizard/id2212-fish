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
    
    public ServerStatistics(int nClient, int nFiles) {
        this.numClients=nClient;
        this.numFiles=nFiles;
    }
    
    public int getNumClients() {
        return this.numClients;
    }
    
    public int getNumFiles() {
        return this.numFiles;
    }
    
    @Override
    public String printSummary() {
        return "Server statistics\nNumber of Clients = "+
                getNumClients()+"\nNumber of Files = "+getNumFiles();
    }
}
