/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class StatThread extends Thread {

    Client client;
    Integer refreshInterval;

    public StatThread(Client c, Integer refreshInterval) {
        client = c;
        this.refreshInterval = refreshInterval;
    }

    public void run() {
        boolean running = client.isConnected();
        while (running) {
            try {

                client.getStatistics();
                Thread.sleep(refreshInterval);
                running = client.isConnected();
            } catch (InterruptedException ex) {
                Logger.getLogger(StatThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
