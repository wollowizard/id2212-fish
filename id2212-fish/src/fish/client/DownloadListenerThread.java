/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FishPacket;
import fish.packets.PacketType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class DownloadListenerThread extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Client client;
    private Boolean running = true;

    public DownloadListenerThread(ObjectInputStream in, ObjectOutputStream out, Client client) {
        this.in = in;
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {

        while (running) {

  
        }

    }
}
