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

    /**
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @return
     */
    public ObjectInputStream getInStream() {
        return in;
    }

    /**
     *
     * @return
     */
    public ObjectOutputStream getOutStream() {
        return out;
    }

    /**
     *
     * @return
     */
    public Integer getListeningThreadPort() {
        return listeningThreadPort;
    }

    /**
     *
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @param in
     */
    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    /**
     *
     * @param out
     */
    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     *
     * @param listeningThreadPort
     */
    public void setListeningThreadPort(Integer listeningThreadPort) {
        this.listeningThreadPort = listeningThreadPort;
    }
    
    
}
