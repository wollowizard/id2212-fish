/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author alfredo
 */
public class ClientNetworkData {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Integer listeningThreadPort = -2;

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getInStream() {
        return in;
    }

    public ObjectOutputStream getOutStream() {
        return out;
    }

    public Integer getListeningThreadPort() {
        return listeningThreadPort;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }

    public void setListeningThreadPort(Integer listeningThreadPort) {
        this.listeningThreadPort = listeningThreadPort;
    }
    
    
}
