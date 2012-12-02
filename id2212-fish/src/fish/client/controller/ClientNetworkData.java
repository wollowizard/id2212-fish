/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * It contains the network resources of the client, like the socket, the streams and the number of the port the client will accept downloads on
 * @author alfredo
 */
public class ClientNetworkData {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Integer listeningThreadPort = -2;

    /**
     * Returns the socket
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * returns the input stream associated to the socket
     * @return the input stream
     */
    public ObjectInputStream getInStream() {
        return in;
    }

    /**
     * returns the output stream associated to the socket
     * @return the output stream
     */
    public ObjectOutputStream getOutStream() {
        return out;
    }

    /**
     * returns the number of the port the client will accept downloads on
     * @return the port number of the listening thread
     */
    public Integer getListeningThreadPort() {
        return listeningThreadPort;
    }

    /**
     * Sets the socket
     * @param socket the socket to be set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sets the input stream
     * @param in the input stream to be set
     */
    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    /**
     * sets the output stream
     * @param out the iutput stream to be set
     */
    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * Sets the port the client will accept downloads on
     * @param listeningThreadPort the port number
     */
    public void setListeningThreadPort(Integer listeningThreadPort) {
        this.listeningThreadPort = listeningThreadPort;
    }
    
    
}
