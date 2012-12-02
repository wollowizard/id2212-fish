/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The thread that periodically asks for statistics
 * @author alfredo
 */
public class StatThread extends Thread {

    ClientController client;
    Integer refreshInterval;

    /**
     *
     * @param c the client controller
     * @param refreshInterval the refresh interval to ask statistics
     */
    public StatThread(ClientController c, Integer refreshInterval) {
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
